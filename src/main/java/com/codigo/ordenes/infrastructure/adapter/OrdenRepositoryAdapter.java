package com.codigo.ordenes.infrastructure.adapter;

import com.codigo.ordenes.application.port.output.OrdenRepositoryPort;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.entity.OrdenEntity;
import com.codigo.ordenes.infrastructure.mapper.OrdenMapper;
import com.codigo.ordenes.infrastructure.repository.OrdenRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

    private final OrdenRepositoryJpa ordenRepositoryJpa;
    private final OrdenMapper ordenMapper;

    @Override
    public Orden save(Orden orden) {
        // Convertir el modelo de dominio 'Orden' a entidad 'OrdenEntity'
        OrdenEntity entity = ordenMapper.toEntity(orden);

        // Validar que el mapeo no haya dado null
        if (entity == null) {
            throw new IllegalStateException("El mapeo de Orden a OrdenEntity devolvió null");
        }

        // Guardar la entidad en la base de datos utilizando el repositorio JPA
        OrdenEntity savedEntity = ordenRepositoryJpa.save(entity);

        // Convertir la entidad guardada de vuelta al modelo de dominio 'Orden' y devolverlo
        return ordenMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Orden> findById(Long id) {
        // Buscar la entidad por ID y convertirla a modelo de dominio
        return ordenRepositoryJpa.findById(id)
                .map(ordenMapper::toDomain);
    }
    @Override
    public List<Orden> findAll() {
        // Convertir todas las entidades a modelos de dominio
        return ordenRepositoryJpa.findAll().stream()
                .map(ordenMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Orden> findByUsuarioId(Long usuarioId) {
        // Buscar todas las órdenes por el ID de usuario y convertirlas
        return ordenRepositoryJpa.findByUsuarioId(usuarioId).stream()
                .map(ordenMapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public void deleteById(Long id) {
        ordenRepositoryJpa.deleteById(id);
    }
}