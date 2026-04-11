package edu.dosw.parcial.DOSW_ParcialT2.controller;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.RegisterRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.RegisterResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.mappers.UserMapper;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.UserService;
import edu.dosw.parcial.DOSW_ParcialT2.core.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;


    @Test
    void register_exitoso_retorna201() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Juan Lopez");
        request.setEmail("juan@mail.escuelaing.edu.co");
        request.setContrasena("Pass123!");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());

        RegisterResponse response = RegisterResponse.builder()
                .id(user.getId())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .build();

        when(userMapper.toDomain(request)).thenReturn(user);
        when(userService.register(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        ResponseEntity<RegisterResponse> result = userController.register(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(request.getEmail(), result.getBody().getEmail());
    }

    @Test
    void register_emailDuplicado_lanzaExcepcion() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("juan@mail.escuelaing.edu.co");
        request.setContrasena("Pass123!");

        User user = new User();
        when(userMapper.toDomain(request)).thenReturn(user);
        when(userService.register(user)).thenThrow(new RuntimeException("El correo ya se encuentra registrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userController.register(request));

        assertEquals("El correo ya se encuentra registrado", ex.getMessage());
    }



    @Test
    void login_exitoso_retorna200() {
        LoginRequest request = new LoginRequest();
        request.setEmail("juan@mail.escuelaing.edu.co");
        request.setContrasena("Pass123!");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setRole(User.Role.CLIENTE);

        LoginResponse response = LoginResponse.builder()
                .token("jwt-token")
                .tipo("CLIENTE")
                .usuarioId(user.getId())
                .nombre("Juan Lopez")
                .build();

        when(userService.login(request.getEmail(), request.getContrasena())).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwt-token");
        when(userMapper.toLoginResponse(user, "jwt-token")).thenReturn(response);

        ResponseEntity<LoginResponse> result = userController.login(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("jwt-token", result.getBody().getToken());
    }

    @Test
    void login_credencialesInvalidas_lanzaExcepcion() {
        LoginRequest request = new LoginRequest();
        request.setEmail("juan@mail.escuelaing.edu.co");
        request.setContrasena("wrongPassword");

        when(userService.login(request.getEmail(), request.getContrasena()))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userController.login(request));

        assertEquals("Credenciales inválidas", ex.getMessage());
    }
}
