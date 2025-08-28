package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.repositories.UserStatusRepository;

import java.util.Optional;

@Service
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;

    public UserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    public UserStatus getByName (String name){
        Optional<UserStatus> optionalUserStatus = userStatusRepository.getByName(name);
        if (optionalUserStatus.isPresent()) return optionalUserStatus.get();
        else throw new EntityNotFoundException("No existe ese estado con ese nombre: " + name);
    }
}
