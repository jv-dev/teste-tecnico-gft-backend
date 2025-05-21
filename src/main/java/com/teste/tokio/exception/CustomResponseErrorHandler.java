package com.teste.tokio.exception;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        String body = new Scanner(response.getBody(), StandardCharsets.UTF_8)
                .useDelimiter("\\A")
                .next();

        throw new RestClientResponseException(
                "External API call failed",
                response.getStatusCode().value(),
                response.getStatusText(),
                response.getHeaders(),
                body.getBytes(),
                StandardCharsets.UTF_8
        );
    }
}
