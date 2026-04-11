package edu.dosw.parcial.DOSW_ParcialT2.core.validators;

import edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validateEmailNotTaken_emailDisponible_noLanzaExcepcion() {
        when(userRepository.existsByEmail("juan@mail.escuelaing.edu.co")).thenReturn(false);

        assertDoesNotThrow(() -> userValidator.validateEmailNotTaken("juan@mail.escuelaing.edu.co"));

        verify(userRepository).existsByEmail("juan@mail.escuelaing.edu.co");
    }

    @Test
    void validateEmailNotTaken_emailOcupado_lanzaExcepcion() {
        when(userRepository.existsByEmail("juan@mail.escuelaing.edu.co")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userValidator.validateEmailNotTaken("juan@mail.escuelaing.edu.co"));

        assertEquals("El correo ya se encuentra registrado", ex.getMessage());
        verify(userRepository).existsByEmail("juan@mail.escuelaing.edu.co");
    }
}
