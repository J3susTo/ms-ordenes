package com.codigo.ordenes.infrastructure.controller;

import com.codigo.ordenes.application.port.input.OrdenUseCase;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.dto.UsuarioAuthDTO;
import com.codigo.ordenes.application.dto.CrearOrdenRequest;
import com.codigo.ordenes.application.dto.ProductoCantidadDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenUseCase ordenUseCase;

    @PostMapping
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<Orden> crearOrden(@RequestBody CrearOrdenRequest request, HttpServletRequest httpRequest) {
        Long usuarioId = (Long) httpRequest.getAttribute("usuarioId");

        Orden orden = Orden.builder()
                .usuarioId(usuarioId)
                .idCliente(request.getIdCliente())
                .productosIds(
                        request.getProductos().stream()
                                .map(ProductoCantidadDTO::getIdProducto)
                                .toList()
                )
                .metodoPago(request.getMetodoPago())
                .build();

        Orden nuevaOrden = ordenUseCase.crearOrden(orden);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<List<Orden>> listarOrdenes() {
        List<Orden> ordenes = ordenUseCase.listarOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN') or @ordenAutorizacionService.esPropioDeLaOrden(#id)")
    public ResponseEntity<Orden> obtenerOrdenPorId(@PathVariable Long id) {
        Orden orden = ordenUseCase.buscarOrdenPorId(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/usuario")
    @PreAuthorize("hasRole('USUARIO') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
    public ResponseEntity<List<Orden>> obtenerOrdenesDelUsuario(HttpServletRequest request) {
        // Obtener ID del usuario del token
        Long usuarioId = (Long) request.getAttribute("usuarioId");

        List<Orden> ordenes = ordenUseCase.buscarOrdenesPorUsuario(usuarioId);
        return ResponseEntity.ok(ordenes);
    }

    // Método auxiliar para obtener el usuario autenticado del contexto de seguridad
    private UsuarioAuthDTO getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UsuarioAuthDTO) {
            return (UsuarioAuthDTO) authentication.getPrincipal();
        }
        return null;
    }
}