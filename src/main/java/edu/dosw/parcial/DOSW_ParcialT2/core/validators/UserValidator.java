package edu.dosw.parcial.DOSW_ParcialT2.core.validators;

import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.UserRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateEmailNotTaken(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Email ya registrado: {}", email);
            throw new RuntimeException("El correo ya se encuentra registrado");
        }
    }
}
