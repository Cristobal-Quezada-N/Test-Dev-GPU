package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.dtos.RegisterRequestDTO;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.enums.UserStatusCode;
import usach.hackaton.gpu.exception.EmailAlreadyRegisteredException;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AuthFactorRepository authFactorRepository;
    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final UserStatusService userStatusService;
    private final PasswordEncoder passwordEncoder;

    public void activateUser(AppUser user) {
        UserStatus activeStatus = userStatusService.getByCode(UserStatusCode.ACTIVE);
        user.setStatus(activeStatus);
        save(user);
    }

    public void checkEmailNotRegistered(String email) {
        if (findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    public Optional<AppUser> findById(UUID userId) {
        return appUserRepository.findById(userId);
    }

    public AppUser getByEmail(String email) {
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent())
            return optionalAppUser.get();
        else
            throw new EntityNotFoundException("No se encuentra el usuario con el email: " + email);
    }

    public AppUser save(AppUser user) {
        return appUserRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public void deleteUserAndAuthFactors(UUID id) {
        // 1. Buscar factores de autenticación por userId
        List<AuthFactor> factors = authFactorRepository.findByUserId(id);

        // 2. Borrar todos los factores asociados
        if (!factors.isEmpty()) {
            authFactorRepository.deleteAll(factors);
        }

        // 3. Finalmente borrar el usuario
        appUserRepository.deleteById(id);
    }

    public Optional<AppUser> getUserById(UUID id) {
        return appUserRepository.findById(id);
    }

    public AppUser updateStatus(UUID id, UserStatus userStatus) {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }

        AppUser user = optionalUser.get();
        user.setStatus(userStatus);
        return save(user);
    }

    public AppUser createPendingUser(RegisterRequestDTO dto) {
        Role role = roleService.getByCode("USER");

        UserStatus userStatus = userStatusService.getByCode(UserStatusCode.PENDING);

        AppUser newUser = AppUser.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(role)
            .status(userStatus)
            .build();
        return save(newUser);

    }
}
