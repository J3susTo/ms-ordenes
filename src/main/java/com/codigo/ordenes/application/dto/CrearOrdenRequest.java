package com.codigo.ordenes.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrearOrdenRequest {
    private Long idCliente;
    private List<ProductoCantidadDTO> productos;
    private String metodoPago;
}
