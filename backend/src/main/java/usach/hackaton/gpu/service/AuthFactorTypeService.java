package usach.hackaton.gpu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.AuthFactorType;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;
import usach.hackaton.gpu.repositories.AuthFactorTypeRepository;

@Service
@RequiredArgsConstructor
public class AuthFactorTypeService {

    private final AuthFactorTypeRepository repository;

    public AuthFactorTypeLookup getByEnum(AuthFactorType enumType) {
        return repository.findByCode(enumType.name())
            .orElseThrow(() -> new IllegalStateException(
                "Auth factor type not found: " + enumType.name()));
    }
}
