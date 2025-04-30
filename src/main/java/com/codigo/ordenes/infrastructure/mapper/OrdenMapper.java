package com.codigo.ordenes.infrastructure.mapper;

import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.entity.OrdenEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenMapper {

    public Orden toDomain(OrdenEntity entity) {
        if (entity == null) {
            return null;
        }

        return Orden.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .productosIds(entity.getProductosIds())
                .fecha(entity.getFecha())
                .estado(mapToEstadoOrdenDomain(entity.getEstado()))
                .build();
    }

    public OrdenEntity toEntity(Orden domain) {
        if (domain == null) {
            return null;
        }

        return OrdenEntity.builder()
                .id(domain.getId())
                .usuarioId(domain.getUsuarioId())
                .productosIds(domain.getProductosIds())
                .fecha(domain.getFecha())
                .estado(mapToEstadoOrdenEntity(domain.getEstado()))
                .build();
    }

    public List<Orden> toDomainList(List<OrdenEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Orden.EstadoOrden mapToEstadoOrdenDomain(OrdenEntity.EstadoOrden entityEstado) {
        if (entityEstado == null) {
            return null;
        }

        return switch (entityEstado) {
            case PENDIENTE -> Orden.EstadoOrden.PENDIENTE;
            case COMPLETADA -> Orden.EstadoOrden.COMPLETADA;
            case CANCELADA -> Orden.EstadoOrden.CANCELADA;
        };
    }

    private OrdenEntity.EstadoOrden mapToEstadoOrdenEntity(Orden.EstadoOrden domainEstado) {
        if (domainEstado == null) {
            return null;
        }

        return switch (domainEstado) {
            case PENDIENTE -> OrdenEntity.EstadoOrden.PENDIENTE;
            case COMPLETADA -> OrdenEntity.EstadoOrden.COMPLETADA;
            case CANCELADA -> OrdenEntity.EstadoOrden.CANCELADA;
        };
    }
}