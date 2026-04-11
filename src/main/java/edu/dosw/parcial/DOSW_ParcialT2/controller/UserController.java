package edu.dosw.parcial.DOSW_ParcialT2.controller;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.RegisterRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.RegisterResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.mappers.UserMapper;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.UserService;
import edu.dosw.parcial.DOSW_ParcialT2.core.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints for user authentication and registration")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("POST /api/users/register - email: {}", request.getEmail());
        User user = userService.register(userMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and get a JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/users/login - email: {}", request.getEmail());
        User user = userService.login(request.getEmail(), request.getContrasena());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(userMapper.toLoginResponse(user, token));
    }
}
