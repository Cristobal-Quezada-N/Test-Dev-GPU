package usach.hackaton.gpu.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.config.JwtUtil;
import usach.hackaton.gpu.dtos.RegisterRequestDTO;
import usach.hackaton.gpu.entities.*;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;
import usach.hackaton.gpu.repositories.TokenRepository;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final UserStatusService userStatusService;
    private final AuthFactorRepository authFactorRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    public AuthService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, RoleService roleService, UserStatusService userStatusService, AuthFactorRepository authFactorRepository, EmailService emailService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.roleService = roleService;
        this.userStatusService = userStatusService;
        this.authFactorRepository = authFactorRepository;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    public Map<String, Object> login(String email, String password) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Contraseña inválida");
        }

        String token = jwtUtil.create(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "roleId", user.getRoleId()
        ));
        return response;
    }


    public void register(RegisterRequestDTO dto){
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email ya registrado");
        }

        Role role = roleService.getByName("Usuario");

        UserStatus userStatus = userStatusService.getByName("Pendiente");

        AppUser newUser = new AppUser();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRoleId(role.getId());
        newUser.setStatusId(userStatus.getId());
        userRepository.save(newUser);

        // Factor de registro
        AuthFactor factor = new AuthFactor();
        factor.setName("Registro inicial");
        factor.setType(AuthFactorType.REGISTER);
        factor.setUsed(true);
        factor.setCreationDate(LocalDate.now());
        factor.setExpirationDate(LocalDate.now().plusYears(1));
        factor.setUserId(newUser.getId());
        authFactorRepository.save(factor);

        String token = generateToken();
        ActivationToken activationToken = ActivationToken.builder()
                .emailToken(token)
                .userId(newUser.getId())
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(activationToken);

        String link = System.getProperty("APP_HOST") + "/api/auth/activate?token=" + token;

        emailService.send(newUser.getEmail(), "Activa tu cuenta",
                "Haz click en este enlace para activar tu cuenta: " + link);
    }

    private String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public boolean activateUser(String token) {
        ActivationToken activationToken = tokenRepository.findByEmailToken(token)
                .orElse(null);

        if (activationToken == null) {
            return false;
        }

        // Validar expiración
        if (activationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(activationToken);
            return false;
        }

        // Activar usuario
        AppUser user = userRepository.findById(activationToken.getUserId())
                .orElse(null);
        if (user == null) return false;

        UserStatus activeStatus = userStatusService.getByName("Activo");
        user.setStatusId(activeStatus.getId());
        userRepository.save(user);

        // Borrar token para que no se pueda reutilizar
        tokenRepository.delete(activationToken);

        return true;
    }

    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }

}
