package com.teste.tokio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthRequest {

    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456")
    private String password;
}
