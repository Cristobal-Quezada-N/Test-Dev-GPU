package usach.hackaton.gpu.service;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.enums.UserStatusCode;
import usach.hackaton.gpu.exception.AccountBannedException;
import usach.hackaton.gpu.exception.AccountNotVerificatedException;
import usach.hackaton.gpu.repositories.ActivationTokenRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleService roleService;
    private final UserStatusService userStatusService;
    private final AuthFactorRepository authFactorRepository;
    private final AuthFactorService authFactorService;
    private final EmailService emailService;
    private final ActivationTokenRepository activationTokenRepository;
    private final ActivationTokenService activationTokenService;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<AppUser> OptionalUser = userService.findByEmail(loginRequest.email());

        if (OptionalUser.isEmpty()
            || !passwordEncoder.matches(loginRequest.password(), OptionalUser.get().getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        AppUser user = OptionalUser.get();

        final UserStatus userStatus = userStatusService.getById(user.getStatusId());
        final UserStatusCode statusCode = UserStatusCode.valueOf(userStatus.getCode());

        switch (statusCode) {
            case ACTIVE -> {
            }
            case PENDING -> throw new AccountNotVerificatedException();
            case BANNED -> throw new AccountBannedException();
            default -> throw new BadCredentialsException("Estado de cuenta inválido. Contacta al administrador");
        }

        final String token = jwtUtil.create(user.getEmail());
        final Role role = roleService.getById(user.getRoleId());
        return new LoginResponse(
            user.getId().toString(),
            user.getEmail(),
            statusCode.name(),
            role.getCode(),
            token
        );
    }

    @Transactional
    public void register(RegisterRequestDTO dto) {
        userService.checkEmailNotRegistered(dto.getEmail());

        AppUser newUser = userService.createPendingUser(dto);

        authFactorService.createRegisterFactor(newUser.getId());

        ActivationToken newRegisterActivationToken = activationTokenService.createActivationToken(newUser);

        emailService.sendActivationEmail(
            newUser.getEmail(),
            newRegisterActivationToken.getToken()
        );
    }

    @Transactional
    public boolean activateUser(String token) {
        Optional<ActivationToken> optionalActivationToken = activationTokenRepository.findByToken(token);

        if (optionalActivationToken.isEmpty()) {
            return false;
        }

        ActivationToken activationToken = optionalActivationToken.get();
        boolean isTokenExpired = activationToken.getExpirationDate().isBefore(LocalDateTime.now());

        // Validar expiración
        if (isTokenExpired) {
            activationTokenRepository.delete(activationToken);
            return false;
        }

        // Activar usuario
        AppUser activatiedUser = activationToken.getUser();
        if (activatiedUser == null)
            return false;

        UserStatus activeStatus = userStatusService.getByCode(UserStatusCode.ACTIVE);
        activatiedUser.setStatusId(activeStatus.getId());
        userService.save(activatiedUser);

        activationTokenService.delete(activationToken);

        return true;
    }

    @Transactional
    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }
}
