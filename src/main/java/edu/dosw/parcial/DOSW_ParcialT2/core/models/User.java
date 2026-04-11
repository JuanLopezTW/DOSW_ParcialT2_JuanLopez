package edu.dosw.parcial.DOSW_ParcialT2.core.models;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public enum Role {
        CLIENTE,
        ADMIN
    }
}
