package usach.hackaton.gpu.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.UserStatus;
import usach.hackaton.gpu.enums.UserStatusCode;
import usach.hackaton.gpu.repositories.UserStatusRepository;

@Service
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;

    public UserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    public UserStatus getById(Long id) {
        return userStatusRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No se encontro `UserStatus` con id: " + id));
    }

    public UserStatus getByCode(UserStatusCode code) {
        final String codeString = code.name();
        Optional<UserStatus> optionalUserStatus = userStatusRepository.findByCode(codeString);
        if (optionalUserStatus.isPresent())
            return optionalUserStatus.get();
        else
            throw new EntityNotFoundException("No existe ese estado de usuario tipo: " + codeString);
    }
}
