package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.repositories.UserStatusRepository;

@Service
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;

    public UserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    public UserStatus getByCode(String code) {
        Optional<UserStatus> optionalUserStatus = userStatusRepository.getByCode(code);
        if (optionalUserStatus.isPresent())
            return optionalUserStatus.get();
        else
            throw new EntityNotFoundException("No existe ese estado de usuario tipo: " + code);
    }
}
