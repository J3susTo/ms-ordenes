package com.codigo.ordenes.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "cliente_id", nullable = false)
    private Long idCliente;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;
    @ElementCollection
    @CollectionTable(
            name = "orden_productos",
            joinColumns = @JoinColumn(name = "orden_id")
    )
    @Column(name = "producto_id")
    private List<Long> productosIds = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    public enum EstadoOrden {
        PENDIENTE, COMPLETADA, CANCELADA
    }
}
