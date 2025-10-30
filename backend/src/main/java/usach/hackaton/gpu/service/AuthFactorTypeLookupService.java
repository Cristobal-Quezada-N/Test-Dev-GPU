package usach.hackaton.gpu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.AuthFactorType;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;
import usach.hackaton.gpu.repositories.AuthFactorTypeLookupRepository;

@Service
@RequiredArgsConstructor
public class AuthFactorTypeLookupService {

    private final AuthFactorTypeLookupRepository repository;

    public AuthFactorTypeLookup getByCode(AuthFactorType code) {
        final String codeString = code.name();
        return repository.findByCode(codeString)
            .orElseThrow(() -> new IllegalStateException(
                "Auth factor type not found: " + codeString));
    }
}
