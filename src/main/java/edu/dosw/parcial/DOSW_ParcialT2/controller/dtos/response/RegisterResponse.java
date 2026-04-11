package edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private UUID id;
    private String nombre;
    private String email;
}