package com.example.sena.Configuration.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.sena.Configuration.security.filter.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final LogoutHandler logoutHandler;

    /**
    Configura el filtro de seguridad que maneja el proceso de autenticación y autorización.
    @param http La configuración de seguridad HTTP.
    @return La cadena de filtros de seguridad.
    @throws Exception Si hay un error en la configuración.
    */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors().configurationSource(new CorsConfigurationSource() {
                    /**
                    Devuelve una configuración CORS personalizada que permite peticiones desde cualquier lugar
                    con cualquier método, encabezado y credenciales, con una edad máxima de caché de 3600 segundos.
                    @param request El objeto HttpServletRequest.
                    @return La configuración CORS personalizada.
                    */
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(List.of("*"));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS", "DELETE", "PUT"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }})
                .and()
                .csrf().disable()
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .requestMatchers( "api/v1/usuarios/registrar", "api/v1/usuarios/paginate" , "api/v1/usuarios/updated/**").hasRole("ADMINISTRADOR")
                .requestMatchers( "api/v1/productos/inhabilitar/**", "api/v1/productos/habilitar/**", "api/v1/productos/filtered"
                                    , "api/v1/productos/codigo/**", "api/v1/productos/codigo-disponible", "api/v1/productos/update/**", 
                                    "api/v1/productos/delete/**", "api/v1/productos/create**", "api/v1/productos/paginate").hasAnyRole("ADMINISTRADOR", "ADMINISTRADOR_PRESTAMOS")
                .requestMatchers("api/v1/auth/login").permitAll()
                .and()
                .logout()
                .logoutUrl("/api/v1/auth/logout").permitAll()
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
