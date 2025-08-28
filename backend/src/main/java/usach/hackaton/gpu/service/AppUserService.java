package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.*;
import usach.hackaton.gpu.repositories.AppUserRepository;
import usach.hackaton.gpu.repositories.AuthFactorRepository;
import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser getByEmail (String email){
        Optional<AppUser> optionalAppUser = appUserRepository.findByEmail(email);
        if (optionalAppUser.isPresent()) return optionalAppUser.get();
        else throw new EntityNotFoundException("No se encuentra el usuario con el email: " + email);
    }

    public void save (AppUser user) {
        appUserRepository.save(user);
    }

}
