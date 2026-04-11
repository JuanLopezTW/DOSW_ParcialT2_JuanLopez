package edu.dosw.parcial.DOSW_ParcialT2.persistence.mappers;

import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        return user;
    }
}
