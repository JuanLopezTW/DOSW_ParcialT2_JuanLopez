package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String tipo;
    private UUID usuarioId;
    private String nombre;
}
