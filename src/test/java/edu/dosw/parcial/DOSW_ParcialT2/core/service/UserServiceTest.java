package edu.dosw.parcial.DOSW_ParcialT2.core.service;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.UserService;
import edu.dosw.parcial.DOSW_ParcialT2.core.validators.UserValidator;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.UserEntity;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers.UserPersistenceMapper;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserPersistenceMapper userPersistenceMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_exitoso() {
        User user = new User();
        user.setEmail("juan@mail.escuelaing.edu.co");
        user.setPassword("Pass123!");

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail(user.getEmail());

        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userPersistenceMapper.toEntity(any())).thenReturn(entity);
        when(userRepository.save(any())).thenReturn(entity);
        when(userPersistenceMapper.toDomain(entity)).thenReturn(user);

        User result = userService.register(user);

        assertNotNull(result);
        verify(userValidator).validateEmailNotTaken(user.getEmail());
        verify(userRepository).save(any());
    }

    @Test
    void register_emailDuplicado_lanzaExcepcion() {
        User user = new User();
        user.setEmail("juan@mail.escuelaing.edu.co");
        user.setPassword("Pass123!");

        doThrow(new RuntimeException("El correo ya se encuentra registrado"))
                .when(userValidator).validateEmailNotTaken(user.getEmail());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.register(user));

        assertEquals("El correo ya se encuentra registrado", ex.getMessage());
        verify(userRepository, never()).save(any());
    }


    @Test
    void login_exitoso() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail("juan@mail.escuelaing.edu.co");
        entity.setPassword("hashedPassword");
        entity.setRole(User.Role.CLIENTE);

        User user = new User();
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());

        when(userRepository.findByEmail("juan@mail.escuelaing.edu.co")).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("Pass123!", entity.getPassword())).thenReturn(true);
        when(userPersistenceMapper.toDomain(entity)).thenReturn(user);

        User result = userService.login("juan@mail.escuelaing.edu.co", "Pass123!");

        assertNotNull(result);
        assertEquals("juan@mail.escuelaing.edu.co", result.getEmail());
    }

    @Test
    void login_emailNoEncontrado_lanzaExcepcion() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login("noexiste@mail.escuelaing.edu.co", "Pass123!"));

        assertEquals("Credenciales inválidas", ex.getMessage());
    }

    @Test
    void login_contrasenaIncorrecta_lanzaExcepcion() {
        UserEntity entity = new UserEntity();
        entity.setEmail("juan@mail.escuelaing.edu.co");
        entity.setPassword("hashedPassword");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userService.login("juan@mail.escuelaing.edu.co", "wrongPassword"));

        assertEquals("Credenciales inválidas", ex.getMessage());
    }
}