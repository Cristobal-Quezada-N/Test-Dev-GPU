package usach.hackaton.gpu.service;

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
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.exception.AccountBannedException;
import usach.hackaton.gpu.exception.AccountNotVerificatedException;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthFactorRepository authFactorRepository;
    private final AuthFactorService authFactorService;
    private final EmailService emailService;
    private final ActivationTokenService activationTokenService;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<AppUser> OptionalUser = userService.findByEmail(loginRequest.email());

        if (OptionalUser.isEmpty()
            || !passwordEncoder.matches(loginRequest.password(), OptionalUser.get().getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        AppUser user = OptionalUser.get();

        final UserStatus userStatus = user.getStatus();

        if (userStatus.isPending()) {
            throw new AccountNotVerificatedException();
        }

        if (userStatus.isBanned()) {
            throw new AccountBannedException();
        }

        final String token = jwtUtil.create(user.getEmail());
        final String roleCode = user.getRole().getCode();
        return new LoginResponse(
            user.getId().toString(),
            user.getEmail(),
            userStatus.getCode(),
            roleCode,
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
    public boolean activateUser(String rawActivationToken) {
        ActivationToken activationToken = activationTokenService.validateAndUse(rawActivationToken);

        if (activationToken == null) {
            return false;
        }

        AppUser activateUser = activationToken.getUser();
        userService.activateUser(activateUser);

        activationTokenService.delete(activationToken);

        return true;
    }

    @Transactional
    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }
}
