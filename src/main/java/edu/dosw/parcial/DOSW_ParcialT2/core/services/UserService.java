package edu.dosw.parcial.DOSW_ParcialT2.core.services;


import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.UserValidator;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.UserEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.UserPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserPersistenceMapper userPersistenceMapper;
    private final PasswordEncoder passwordEncoder;

    public User register(User user) {
        userValidator.validateEmailNotTaken(user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CLIENTE);

        UserEntity saved = userRepository.save(userPersistenceMapper.toEntity(user));
        log.info("usuario registrado con el email: {}", user.getEmail());

        return userPersistenceMapper.toDomain(saved);
    }

    public User login(String email, String password) {
        UserEntity entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, entity.getPassword())) {
            log.warn("login fallido para el email: {}", email);
            throw new RuntimeException("Credenciales inválidas");
        }

        log.info("login exitoso para el email: {}", email);
        return userPersistenceMapper.toDomain(entity);
    }
}