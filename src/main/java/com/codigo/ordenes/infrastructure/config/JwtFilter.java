package com.codigo.ordenes.infrastructure.config;

import com.codigo.ordenes.infrastructure.client.AuthFeignClient;
import com.codigo.ordenes.infrastructure.client.dto.UsuarioAuthDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AuthFeignClient authFeignClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing or invalid");
            return;
        }

        try {
            String token = authHeader.substring(7); // Extraer el token

            // 📌 LOG 1: Mostrar token recibido
            log.info("Validando token: {}", token);

            // Validar el token con Feign Client
            UsuarioAuthDTO usuarioAuth = authFeignClient.validateToken("Bearer " + token);

            // 📌 LOG 2: Mostrar respuesta de ms-auth
            log.info("Respuesta del microservicio de auth: {}", usuarioAuth);

            if (usuarioAuth != null && usuarioAuth.isValid()) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        usuarioAuth,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuarioAuth.getRol()))
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("usuarioId", usuarioAuth.getId());
                request.setAttribute("usuarioRol", usuarioAuth.getRol());
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT invalido o expirado");
                return;
            }
        } catch (Exception e) {
            log.error("Error validando token JWT: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error al validar el token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
