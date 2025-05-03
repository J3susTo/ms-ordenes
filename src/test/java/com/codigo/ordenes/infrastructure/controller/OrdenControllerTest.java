package com.codigo.ordenes.controller;

import com.codigo.ordenes.application.service.OrdenService;
import com.codigo.ordenes.domain.model.Orden;
import com.codigo.ordenes.infrastructure.client.ProductoFeignClient;
import com.codigo.ordenes.infrastructure.client.dto.ProductoDTO;
import com.codigo.ordenes.infrastructure.controller.OrdenController;
import com.codigo.ordenes.infrastructure.mapper.OrdenMapper;
import com.codigo.ordenes.infrastructure.service.OrdenAutorizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenController.class) // Se indica que es un test para el controlador
public class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private OrdenService ordenService;
    private ProductoFeignClient productoFeignClient;
    private OrdenMapper ordenMapper;
    private OrdenAutorizacionService ordenAutorizacionService;

    @BeforeEach
    void setUp() {
        // Simulación de las dependencias del controlador
        ordenService = mock(OrdenService.class);
        productoFeignClient = mock(ProductoFeignClient.class);
        ordenMapper = new OrdenMapper();
        ordenAutorizacionService = mock(OrdenAutorizacionService.class);
    }

    @Test
    void crearOrden_DeberiaCrearOrden() throws Exception {
        // Arrange
        Long productoId = 1L;
        Orden orden = Orden.builder()
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(productoId))
                .metodoPago("tarjeta")
                .build();

        when(productoFeignClient.verificarProducto(eq(productoId), anyString()))
                .thenReturn(new ProductoDTO()); // Simula que el producto existe
        when(ordenService.crearOrden(any(Orden.class)))
                .thenReturn(orden); // Simula la creación de la orden

        // Act & Assert: Se hace una petición POST a /ordenes
        mockMvc.perform(post("/ordenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":100, \"idCliente\":200, \"productosIds\":[1], \"metodoPago\":\"tarjeta\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuarioId").value(100))
                .andExpect(jsonPath("$.idCliente").value(200))
                .andExpect(jsonPath("$.productosIds[0]").value(1))
                .andExpect(jsonPath("$.metodoPago").value("tarjeta"));

        verify(ordenService, times(1)).crearOrden(any(Orden.class)); // Verifica que el método se haya llamado una vez
    }

    @Test
    void obtenerOrden_DeberiaRetornarOrdenPorId() throws Exception {
        // Arrange
        Long ordenId = 1L;
        Orden orden = Orden.builder()
                .id(ordenId)
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(1L))
                .metodoPago("tarjeta")
                .build();

        when(ordenService.buscarOrdenPorId(ordenId)).thenReturn(orden);

        // Act & Assert: Se hace una petición GET a /ordenes/{id}
        mockMvc.perform(get("/ordenes/{id}", ordenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ordenId))
                .andExpect(jsonPath("$.usuarioId").value(100))
                .andExpect(jsonPath("$.idCliente").value(200))
                .andExpect(jsonPath("$.productosIds[0]").value(1))
                .andExpect(jsonPath("$.metodoPago").value("tarjeta"));
    }

    @Test
    void obtenerOrden_DeberiaRetornarErrorSiNoExiste() throws Exception {
        // Arrange
        Long ordenId = 1L;

        when(ordenService.buscarOrdenPorId(ordenId)).thenThrow(new RuntimeException("Orden no encontrada"));

        // Act & Assert: Se hace una petición GET a /ordenes/{id}
        mockMvc.perform(get("/ordenes/{id}", ordenId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Orden no encontrada"));
    }

    @Test
    void actualizarOrden_DeberiaActualizarOrden() throws Exception {
        // Arrange
        Long ordenId = 1L;
        Orden orden = Orden.builder()
                .id(ordenId)
                .usuarioId(100L)
                .idCliente(200L)
                .productosIds(List.of(1L))
                .metodoPago("efectivo")
                .build();

        when(ordenService.actualizarOrden(eq(ordenId), any(Orden.class))).thenReturn(orden);

        // Act & Assert: Se hace una petición PUT a /ordenes/{id}
        mockMvc.perform(put("/ordenes/{id}", ordenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":100, \"idCliente\":200, \"productosIds\":[1], \"metodoPago\":\"efectivo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ordenId))
                .andExpect(jsonPath("$.metodoPago").value("efectivo"));
    }

    @Test
    void eliminarOrden_DeberiaEliminarOrden() throws Exception {
        // Arrange
        Long ordenId = 1L;

        doNothing().when(ordenService).eliminarOrden(ordenId);

        // Act & Assert: Se hace una petición DELETE a /ordenes/{id}
        mockMvc.perform(delete("/ordenes/{id}", ordenId))
                .andExpect(status().isNoContent());

        verify(ordenService, times(1)).eliminarOrden(ordenId); // Verifica que el método se haya llamado una vez
    }
}
