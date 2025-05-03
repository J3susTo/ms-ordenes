package com.codigo.ordenes.infrastructure.service;

import com.codigo.ordenes.application.port.input.OrdenUseCase;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.dto.UsuarioAuthDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrdenAutorizacionServiceTest {

    private OrdenUseCase ordenUseCase;
    private OrdenAutorizacionService service;

    @BeforeEach
    void setUp() {
        ordenUseCase = mock(OrdenUseCase.class);
        service = new OrdenAutorizacionService(ordenUseCase);
    }

    @Test
    void testEsPropioDeLaOrdenTrue() {
        UsuarioAuthDTO usuario = new UsuarioAuthDTO();
        usuario.setId(10L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario, null)
        );

        Orden orden = Orden.builder().id(1L).usuarioId(10L).build();
        when(ordenUseCase.buscarOrdenPorId(1L)).thenReturn(orden);

        boolean resultado = service.esPropioDeLaOrden(1L);

        assertThat(resultado).isTrue();
    }

    @Test
    void testEsPropioDeLaOrdenFalsePorOtroUsuario() {
        UsuarioAuthDTO usuario = new UsuarioAuthDTO();
        usuario.setId(20L);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(usuario, null)
        );

        Orden orden = Orden.builder().id(1L).usuarioId(10L).build();
        when(ordenUseCase.buscarOrdenPorId(1L)).thenReturn(orden);

        boolean resultado = service.esPropioDeLaOrden(1L);

        assertThat(resultado).isFalse();
    }

    @Test
    void testEsPropioDeLaOrdenFalsePorFaltaDeUsuario() {
        SecurityContextHolder.clearContext();

        Orden orden = Orden.builder().id(1L).usuarioId(10L).build();
        when(ordenUseCase.buscarOrdenPorId(1L)).thenReturn(orden);

        boolean resultado = service.esPropioDeLaOrden(1L);

        assertThat(resultado).isFalse();
    }
}
