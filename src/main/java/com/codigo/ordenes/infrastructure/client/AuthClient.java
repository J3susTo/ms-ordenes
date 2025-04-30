package com.codigo.ordenes.infrastructure.client;

import com.codigo.ordenes.infrastructure.client.dto.UsuarioAuthDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AuthClient {

    @Value("${auth.validate.url}")
    private String authValidateUrl;  // URL de validación de ms-auth

    private final RestTemplate restTemplate = new RestTemplate();

    public UsuarioAuthDTO validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UsuarioAuthDTO> response = restTemplate.exchange(
                    authValidateUrl,
                    HttpMethod.GET,
                    requestEntity,
                    UsuarioAuthDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Token inválido o expirado");
        } catch (Exception ex) {
            throw new RuntimeException("Error al validar el token");
        }
    }
}
