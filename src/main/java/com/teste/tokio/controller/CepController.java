package com.teste.tokio.controller;

import com.teste.tokio.dto.ViaCepResponse;
import com.teste.tokio.service.CepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
@RequiredArgsConstructor
public class CepController {

    private final CepService cepService;

    @GetMapping("/{cep}")
    public ViaCepResponse getAddressByCep(@PathVariable String cep) {
        return cepService.getAddressFromCep(cep);
    }
}
