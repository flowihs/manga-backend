package ru.github.mangmania.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateAccountDataResponse {
    private String username;
}
