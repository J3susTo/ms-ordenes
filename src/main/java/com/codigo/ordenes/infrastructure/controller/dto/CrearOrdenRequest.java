package com.codigo.ordenes.infrastructure.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrearOrdenRequest {
    private Long idCliente;
    private List<ProductoCantidadDTO> productos;
    private String metodoPago;
}
