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

@Component
@RequiredArgsConstructor
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

    private final OrdenRepositoryJpa ordenRepositoryJpa;
    private final OrdenMapper ordenMapper;

    @Override
    public Orden save(Orden orden) {
        OrdenEntity entity = ordenMapper.toEntity(orden);
        OrdenEntity savedEntity = ordenRepositoryJpa.save(entity);
        return ordenMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Orden> findById(Long id) {
        return ordenRepositoryJpa.findById(id)
                .map(ordenMapper::toDomain);
    }

    @Override
    public List<Orden> findAll() {
        List<OrdenEntity> entities = ordenRepositoryJpa.findAll();
        return ordenMapper.toDomainList(entities);
    }

    @Override
    public List<Orden> findByUsuarioId(Long usuarioId) {
        List<OrdenEntity> entities = ordenRepositoryJpa.findByUsuarioId(usuarioId);
        return ordenMapper.toDomainList(entities);
    }

    @Override
    public void deleteById(Long id) {
        ordenRepositoryJpa.deleteById(id);
    }
}