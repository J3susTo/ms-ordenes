package com.codigo.ordenes.infrastructure.client;

import com.codigo.ordenes.infrastructure.client.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-productos", path = "/productos")
public interface ProductoFeignClient {

    @GetMapping("/{id}")
    ProductoDTO getProducto(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/verificar/{id}")
    boolean verificarProducto(@PathVariable Long id, @RequestHeader("Authorization") String token);
}