package com.codigo.ordenes.infrastructure.mapper;

import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.entity.OrdenEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrdenMapperTest {

    private final OrdenMapper mapper = new OrdenMapper();

    @Test
    void testToEntity() {
        Orden domain = Orden.builder()
                .id(1L)
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(1L, 2L))
                .metodoPago("Tarjeta")
                .fecha(LocalDateTime.now())
                .estado(Orden.EstadoOrden.PENDIENTE)
                .build();

        OrdenEntity entity = mapper.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(domain.getId());
        assertThat(entity.getEstado().name()).isEqualTo(domain.getEstado().name());
    }

    @Test
    void testToDomain() {
        OrdenEntity entity = OrdenEntity.builder()
                .id(1L)
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(1L, 2L))
                .metodoPago("Efectivo")
                .fecha(LocalDateTime.now())
                .estado(OrdenEntity.EstadoOrden.CANCELADA)
                .build();

        Orden domain = mapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(entity.getId());
        assertThat(domain.getEstado().name()).isEqualTo(entity.getEstado().name());
    }

    @Test
    void testToDomainList() {
        List<OrdenEntity> entities = List.of(
                OrdenEntity.builder().id(1L).estado(OrdenEntity.EstadoOrden.PENDIENTE).build(),
                OrdenEntity.builder().id(2L).estado(OrdenEntity.EstadoOrden.COMPLETADA).build()
        );

        List<Orden> domains = mapper.toDomainList(entities);

        assertThat(domains).hasSize(2);
        assertThat(domains.get(0).getId()).isEqualTo(1L);
        assertThat(domains.get(1).getEstado()).isEqualTo(Orden.EstadoOrden.COMPLETADA);
    }
}