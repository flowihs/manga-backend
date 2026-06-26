package ru.github.mangmania.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.github.mangmania.auth.entity.User;
import ru.github.mangmania.settings.entity.Settings;

@Setter
@Getter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Settings settings;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .settings(user.getSettings())
                .build();
    }
}
