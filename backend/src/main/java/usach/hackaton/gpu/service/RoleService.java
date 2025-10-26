package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.repositories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    Role getByCode(String code) {
        Optional<Role> optionalRole = roleRepository.findByCode(code);
        if (optionalRole.isPresent())
            return optionalRole.get();
        else
            throw new EntityNotFoundException("No se encuentra ese rol");
    }
}
