package com.codigo.ordenes.infrastructure.service;

import com.codigo.ordenes.application.port.input.OrdenUseCase;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.dto.UsuarioAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrdenAutorizacionService {

    private final OrdenUseCase ordenUseCase;

    public boolean esPropioDeLaOrden(Long ordenId) {
        try {
            Orden orden = ordenUseCase.buscarOrdenPorId(ordenId);
            UsuarioAuthDTO usuario = getUsuarioAutenticado();

            if (usuario == null) {
                return false;
            }

            // Verificar si el usuario es dueño de la orden
            return orden.getUsuarioId().equals(usuario.getId());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private UsuarioAuthDTO getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UsuarioAuthDTO) {
            return (UsuarioAuthDTO) authentication.getPrincipal();
        }
        return null;
    }
}