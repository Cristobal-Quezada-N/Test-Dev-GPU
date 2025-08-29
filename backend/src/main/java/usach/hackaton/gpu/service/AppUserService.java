package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.*;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {
    private final AuthFactorRepository authFactorRepository;
    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository, AuthFactorRepository authFactorRepository) {
        this.appUserRepository = appUserRepository;
        this.authFactorRepository = authFactorRepository;
    }

    public AppUser getByEmail (String email){
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent()) return optionalAppUser.get();
        else throw new EntityNotFoundException("No se encuentra el usuario con el email: " + email);
    }

    public void save (AppUser user) {
        appUserRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public void deleteUserAndAuthFactors(String id) {
        // 1. Buscar factores de autenticación por userId (conversión Long → String)
        List<AuthFactor> factors = authFactorRepository.findByUserId(String.valueOf(id));

        // 2. Borrar todos los factores asociados
        if (!factors.isEmpty()) {
            authFactorRepository.deleteAll(factors);
        }

        // 3. Finalmente borrar el usuario
        appUserRepository.deleteById(id);
    }


    public Optional<AppUser> getUserById(String id) {
        return appUserRepository.findById(id);
    }

    public AppUser updateStatus(String id, Long statusId) {
        Optional<AppUser> optionalUser = appUserRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }

        AppUser user = optionalUser.get();
        user.setStatusId(statusId); // 👈 asegúrate de que AppUser tenga este campo y su setter
        return appUserRepository.save(user);
    }

}