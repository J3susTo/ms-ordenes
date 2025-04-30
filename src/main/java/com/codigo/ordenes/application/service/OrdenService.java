package com.codigo.ordenes.application.service;

import com.codigo.ordenes.application.port.input.OrdenUseCase;
import com.codigo.ordenes.application.port.output.OrdenRepositoryPort;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.ProductoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenService implements OrdenUseCase {

    private final OrdenRepositoryPort ordenRepositoryPort;
    private final ProductoFeignClient productoFeignClient;

    @Override
    @Transactional
    public Orden crearOrden(Orden orden) {
        // Verificar que todos los productos existan
        String token = "Bearer " + obtenerTokenContext(); // Esta función debería implementarse para obtener el token del contexto de seguridad

        boolean todosProductosExisten = orden.getProductosIds().stream()
                .allMatch(productoId -> productoFeignClient.verificarProducto(productoId, token));

        if (!todosProductosExisten) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }

        // Establecer la fecha actual y estado pendiente
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);

        return ordenRepositoryPort.save(orden);
    }

    @Override
    public List<Orden> listarOrdenes() {
        return ordenRepositoryPort.findAll();
    }

    @Override
    public Orden buscarOrdenPorId(Long id) {
        return ordenRepositoryPort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Orden no encontrada con ID: " + id));
    }

    @Override
    public List<Orden> buscarOrdenesPorUsuario(Long usuarioId) {
        return ordenRepositoryPort.findByUsuarioId(usuarioId);
    }

    // Método auxiliar para obtener el token del contexto de seguridad
    private String obtenerTokenContext() {
        // Implementación para obtener el token del contexto de seguridad
        // Esto dependerá de cómo estés manejando el token en tu aplicación
        return "token-placeholder";
    }
}