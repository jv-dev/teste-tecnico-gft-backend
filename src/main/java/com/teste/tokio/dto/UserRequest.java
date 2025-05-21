package com.teste.tokio.dto;

import com.teste.tokio.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRequest {

    @Schema(description = "Nome completo do usuário", example = "João Victor Cardoso de Souza")
    private String name;

    @Schema(description = "Email do usuário (deve ser único)", example = "joao@email.com")
    private String email;

    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "123456")
    private String password;

    @Schema(description = "Perfil do usuário", example = "ADMIN")
    private Role role;
}
