package com.codigo.ordenes.infrastructure.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAuthDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;

    private boolean isValid;
}