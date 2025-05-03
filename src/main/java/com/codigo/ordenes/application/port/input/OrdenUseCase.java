package com.codigo.ordenes.application.port.input;

import com.codigo.ordenes.domain.model.Orden;

import java.util.List;

public interface OrdenUseCase {
    Orden crearOrden(Orden orden);
    List<Orden> listarOrdenes();
    Orden buscarOrdenPorId(Long id);
    List<Orden> buscarOrdenesPorUsuario(Long usuarioId);
    Orden actualizarOrden(Long id, Orden orden);
    void eliminarOrden(Long id);
}