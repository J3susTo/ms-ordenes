package com.codigo.ordenes.infrastructure.repository;

import com.codigo.ordenes.infrastructure.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepositoryJpa extends JpaRepository<OrdenEntity, Long> {
    List<OrdenEntity> findByUsuarioId(Long usuarioId);
}