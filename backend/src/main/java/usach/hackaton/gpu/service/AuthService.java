package usach.hackaton.gpu.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.hackaton.gpu.config.JwtUtil;
import usach.hackaton.gpu.dtos.LoginRequest;
import usach.hackaton.gpu.dtos.LoginResponse;
import usach.hackaton.gpu.dtos.RegisterRequestDTO;
import usach.hackaton.gpu.entities.ActivationToken;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.entities.AuthFactorType;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.exception.EmailAlreadyRegisteredException;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;
import usach.hackaton.gpu.repositories.TokenRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final UserStatusService userStatusService;
    private final AuthFactorRepository authFactorRepository;
    private final AuthFactorTypeService authFactorTypeService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Value("${app.url}")
    private String baseUrl;

    public LoginResponse login(LoginRequest loginRequest) {
        AppUser user = userRepository.findByEmail(loginRequest.email())
            .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Contraseña inválida");
        }

        final String token = jwtUtil.create(user.getEmail());
        final UserStatus userStatus = userStatusService.getById(user.getStatusId());
        final Role role = roleService.getById(user.getRoleId());
        return new LoginResponse(user.getId().toString(), user.getEmail(), userStatus.getCode(), role.getCode(), token);
    }

    @Transactional
    public void register(RegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }

        Role role = roleService.getByCode("USER");

        UserStatus userStatus = userStatusService.getByCode("PENDING");

        AppUser newUser = AppUser.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .roleId(role.getId())
            .statusId(userStatus.getId())
            .build();
        userRepository.save(newUser);

        // Factor de registro
        AuthFactorTypeLookup registerType = authFactorTypeService.getByEnum(AuthFactorType.REGISTER);
        AuthFactor factor = AuthFactor.builder()
            .userId(newUser.getId())
            .typeId(registerType)
            .used(true)
            .creationDate(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusYears(1))
            .build();
        authFactorRepository.save(factor);

        String token = generateToken();
        ActivationToken activationToken = ActivationToken.builder()
            .email(token)
            .userId(newUser.getId())
            .expirationDate(LocalDateTime.now().plusHours(24))
            .build();
        tokenRepository.save(activationToken);

        String link = baseUrl + "/api/auth/activate?token=" + token;

        emailService.send(newUser.getEmail(), "Activa tu cuenta",
            "Haz click en este enlace para activar tu cuenta: " + link);
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    public boolean activateUser(String token) {
        ActivationToken activationToken = tokenRepository.findByEmail(token).orElse(null);

        if (activationToken == null) {
            return false;
        }

        // Validar expiración
        if (activationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(activationToken);
            return false;
        }

        // Activar usuario
        AppUser user = userRepository.findById(activationToken.getUserId()).orElse(null);
        if (user == null)
            return false;

        UserStatus activeStatus = userStatusService.getByCode("ACTIVE");
        user.setStatusId(activeStatus.getId());
        userRepository.save(user);

        // Borrar token para que no se pueda reutilizar
        tokenRepository.delete(activationToken);

        return true;
    }

    @Transactional
    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }
}
