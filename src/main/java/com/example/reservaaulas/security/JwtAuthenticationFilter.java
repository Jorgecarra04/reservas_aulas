package com.example.reservaaulas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Si no hay token o no empieza con Bearer → continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer token (sin "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            // 4. Extraer usuario del token
            final String username = jwtService.extractUsername(jwt);

            // 5. Validar si aún no está autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Cargar usuario desde BD
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 7. Validar token
                if (jwtService.validateToken(jwt, userDetails)) {

                    // 8. Crear autenticación
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 9. Colocar autenticación en el contexto
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al validar JWT: " + e.getMessage());
        }

        // 10. Continuar con el resto del filtro
        filterChain.doFilter(request, response);
    }
}
