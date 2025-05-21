package com.teste.tokio.dto;

import com.teste.tokio.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {

    @Schema(description = "Identificador do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "João Victor Cardoso de Souza")
    private String name;

    @Schema(description = "Email do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Perfil do usuário", example = "USER")
    private Role role;

    @Schema(description = "Data de criação", example = "2025-05-20T23:45:00Z")
    private Date createdAt;
}
