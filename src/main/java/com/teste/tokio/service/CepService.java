package com.teste.tokio.service;

import com.teste.tokio.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CepService {

    @Value("${viacep.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ViaCepResponse getAddressFromCep(String cep) {
        String url = baseUrl + cep + "/json/";
        return restTemplate.getForObject(url, ViaCepResponse.class);
    }
}
