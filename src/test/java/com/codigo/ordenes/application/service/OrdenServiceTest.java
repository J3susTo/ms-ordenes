package com.codigo.ordenes.application.service;

import com.codigo.ordenes.application.port.output.OrdenRepositoryPort;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.ProductoFeignClient;
import com.codigo.ordenes.infrastructure.client.dto.ProductoDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdenServiceTest {

    private OrdenService ordenService;
    private OrdenRepositoryPort ordenRepositoryPort;
    private ProductoFeignClient productoFeignClient;

    @BeforeEach
    void setUp() {
        ordenRepositoryPort = mock(OrdenRepositoryPort.class);
        productoFeignClient = mock(ProductoFeignClient.class);
        ordenService = new OrdenService(ordenRepositoryPort, productoFeignClient);
    }

    @Test
    void crearOrden_DeberiaCrearOrdenSiProductosExisten() {
        // Arrange
        Long productoId = 1L;
        Orden orden = Orden.builder()
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(productoId))
                .metodoPago("tarjeta")
                .build();

        when(productoFeignClient.verificarProducto(eq(productoId), anyString()))
                .thenReturn(new ProductoDTO()); // simula que el producto existe

        when(ordenRepositoryPort.save(any(Orden.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // simula que se guarda la orden

        // Mockear el token en RequestContextHolder
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer mock-token");
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);

        try (MockedStatic<RequestContextHolder> mockedStatic = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(attributes);

            // Act
            Orden resultado = ordenService.crearOrden(orden);

            // Assert
            assertNotNull(resultado.getFecha());
            assertEquals(Orden.EstadoOrden.PENDIENTE, resultado.getEstado());
            verify(productoFeignClient, times(1)).verificarProducto(eq(productoId), anyString());
            verify(ordenRepositoryPort, times(1)).save(any(Orden.class));
        }
    }

    @Test
    void crearOrden_DeberiaLanzarExcepcionSiProductoNoExiste() {
        // Arrange
        Long productoId = 2L;
        Orden orden = Orden.builder()
                .usuarioId(101L)
                .idCliente(201L)
                .productosIds(List.of(productoId))
                .metodoPago("efectivo")
                .build();

        when(productoFeignClient.verificarProducto(eq(productoId), anyString()))
                .thenReturn(null); // producto no existe

        // Mock del token
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer otro-token");
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);

        try (MockedStatic<RequestContextHolder> mockedStatic = Mockito.mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::getRequestAttributes).thenReturn(attributes);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> ordenService.crearOrden(orden));
            verify(ordenRepositoryPort, never()).save(any(Orden.class));
        }
    }
}
