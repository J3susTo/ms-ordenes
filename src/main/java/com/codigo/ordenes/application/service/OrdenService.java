package com.codigo.ordenes.application.service;

import com.codigo.ordenes.application.port.input.OrdenUseCase;
import com.codigo.ordenes.application.port.output.OrdenRepositoryPort;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.ProductoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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
        // Obtener el token del contexto de seguridad
        String token = "Bearer " + obtenerTokenContext();  // Llamada al método para obtener el token


        boolean todosProductosExisten = orden.getProductosIds().stream()
                .allMatch(productoId -> productoFeignClient.verificarProducto(productoId, token) != null);

        if (!todosProductosExisten) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }

        // Establecer la fecha actual y estado pendiente
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);

        return ordenRepositoryPort.save(orden);
    }
    // Método auxiliar para obtener el token del contexto de seguridad
    private String obtenerTokenContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            return jwtToken.getToken().getTokenValue();
        }
        throw new IllegalStateException("No se pudo obtener el token JWT");
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
}