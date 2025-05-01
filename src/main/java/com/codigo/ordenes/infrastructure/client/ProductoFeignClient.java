package com.codigo.ordenes.infrastructure.client;

import com.codigo.ordenes.infrastructure.client.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-productos", url = "${servicio.productos.url}")
public interface ProductoFeignClient {

    // Método para obtener un producto por su ID
    @GetMapping("/productos/{id}")
    ProductoDTO getProducto(@PathVariable Long id, @RequestHeader("Authorization") String token);

    // Método para verificar un producto
    @GetMapping("/productos/verificar/{id}")
    ProductoDTO verificarProducto(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}