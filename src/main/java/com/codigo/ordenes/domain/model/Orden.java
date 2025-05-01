package com.codigo.ordenes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orden {
    private Long id;
    private Long usuarioId;
    private Long idCliente;
    private List<Long> productosIds;
    private LocalDateTime fecha;
    private EstadoOrden estado;
    private String metodoPago;

    public enum EstadoOrden {
        PENDIENTE, COMPLETADA, CANCELADA
    }
}