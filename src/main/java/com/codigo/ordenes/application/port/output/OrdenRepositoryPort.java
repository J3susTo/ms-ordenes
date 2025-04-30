package com.codigo.ordenes.application.port.output;

import com.codigo.ordenes.domain.model.Orden;

import java.util.List;
import java.util.Optional;

public interface OrdenRepositoryPort {
    Orden save(Orden orden);
    Optional<Orden> findById(Long id);
    List<Orden> findAll();
    List<Orden> findByUsuarioId(Long usuarioId);
    void deleteById(Long id);
}