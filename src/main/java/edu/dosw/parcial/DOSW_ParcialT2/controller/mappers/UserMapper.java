package edu.dosw.parcial.DOSW_ParcialT2.controller.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.RegisterRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.RegisterResponse;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(RegisterRequest request) {
        User user = new User();
        user.setName(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(request.getContrasena());
        user.setRole(User.Role.CLIENTE);
        return user;
    }

    public User toDomain(LoginRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getContrasena());
        return user;
    }

    public RegisterResponse toResponse(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .nombre(user.getName())
                .email(user.getEmail())
                .build();
    }

    public LoginResponse toLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .tipo(user.getRole().name())
                .usuarioId(user.getId())
                .nombre(user.getName())
                .build();
    }
}
