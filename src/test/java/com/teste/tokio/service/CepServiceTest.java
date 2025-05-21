package com.teste.tokio.service;

import com.teste.tokio.dto.ViaCepResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CepServiceTest {

    @Autowired
    private CepService cepService;

    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    void shouldCallViaCepAndReturnResponse() {
        String cep = "01001000";
        String expectedUrl = "https://viacep.com.br/ws/" + cep + "/json/";

        ViaCepResponse mockResponse = new ViaCepResponse();
        mockResponse.setZipCode("01001-000");
        mockResponse.setStreet("Praça da Sé");
        mockResponse.setCity("São Paulo");

        when(restTemplate.getForObject(expectedUrl, ViaCepResponse.class))
                .thenReturn(mockResponse);

        ViaCepResponse response = cepService.getAddressFromCep(cep);

        assertNotNull(response);
        assertEquals("01001-000", response.getZipCode());
        assertEquals("Praça da Sé", response.getStreet());
        assertEquals("São Paulo", response.getCity());

        verify(restTemplate, times(1)).getForObject(expectedUrl, ViaCepResponse.class);
    }
}
