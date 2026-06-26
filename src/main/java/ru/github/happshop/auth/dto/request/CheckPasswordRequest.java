package ru.github.happshop.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPasswordRequest {
    @NotBlank
    private String password;
}
