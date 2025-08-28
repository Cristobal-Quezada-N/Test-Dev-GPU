package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.Role;
import usach.hackaton.gpu.repositories.RoleRepository;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    Role getByName(String name){
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (optionalRole.isPresent()) return optionalRole.get();
        else throw new EntityNotFoundException("No se encuentra ese rol");
    }
}
