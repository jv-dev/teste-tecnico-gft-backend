package com.teste.tokio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class AddressResponse {

    @Schema(description = "Identificador do endereço", example = "1")
    private Long id;

    @Schema(description = "Logradouro", example = "Rua A")
    private String street;

    @Schema(description = "Número do imóvel", example = "123")
    private String number;

    @Schema(description = "Complemento do endereço", example = "Apto 101")
    private String complement;

    @Schema(description = "Bairro", example = "Centro")
    private String district;

    @Schema(description = "Cidade", example = "São Paulo")
    private String city;

    @Schema(description = "Estado (UF)", example = "SP")
    private String state;

    @Schema(description = "CEP", example = "01000-000")
    private String zipCode;

    @Schema(description = "Data de criação", example = "2025-05-20T23:45:00Z")
    private Date createdAt;
}
