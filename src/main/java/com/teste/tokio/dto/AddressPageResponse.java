package com.teste.tokio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AddressPageResponse {

    @Schema(description = "Lista de endereços retornados")
    private List<AddressResponse> content;

    @Schema(description = "Número da página atual", example = "0")
    private int page;

    @Schema(description = "Quantidade de elementos por página", example = "10")
    private int size;

    @Schema(description = "Total de elementos encontrados", example = "7")
    private long totalElements;

    @Schema(description = "Total de páginas disponíveis", example = "1")
    private int totalPages;
}
