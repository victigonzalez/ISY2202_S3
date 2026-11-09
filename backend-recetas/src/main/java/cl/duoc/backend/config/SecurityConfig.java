package cl.duoc.backend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Seguridad de la API REST.
 *
 * Públicos : GET /api/recetas y GET /api/recetas/buscar
 * Privados : GET /api/recetas/{id} (detalle), con HTTP Basic
 * Cualquier otro método o ruta queda bloqueado (denegado por defecto).
 *
 * La API se sirve solo por HTTPS (ver application.properties): las credenciales
 * de HTTP Basic viajan cifradas y Spring Security agrega HSTS automáticamente.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/recetas", "/api/recetas/buscar").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/recetas/*").authenticated()
                .requestMatchers("/error").permitAll()
                .anyRequest().denyAll()
            )
            .httpBasic(basic -> basic.authenticationEntryPoint(noAutenticado()))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(noAutenticado())
                .accessDeniedHandler(accesoDenegado())
            )
            // API sin estado: no crea sesión ni cookie, se autentica en cada petición.
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // CSRF se desactiva porque la API no usa cookies de sesión (el ataque CSRF depende de ellas)
            // y solo expone métodos GET de lectura.
            .csrf(csrf -> csrf.disable())
            // Cabeceras de seguridad para respuestas JSON
            .headers(h -> h
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'none'; frame-ancestors 'none'"))
                .referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
            );
        return http.build();
    }

    /** 401 en JSON (en vez de la página Whitelabel). */
    private AuthenticationEntryPoint noAutenticado() {
        return (request, response, ex) -> {
            response.setHeader("WWW-Authenticate", "Basic realm=\"recetas-api\"");
            escribirJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }

    /** 403 en JSON. */
    private AccessDeniedHandler accesoDenegado() {
        return (request, response, ex) -> escribirJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }

    private static void escribirJson(HttpServletResponse response, int status, String error) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"status\":" + status + ",\"error\":\"" + error + "\"}");
    }

    /** Mismos 3 usuarios del frontend, con contraseña en hash BCrypt. */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password("$2a$10$qWPhCaai2gntdrboHpSFYuwu8djIi4msf7YVafFD2aHa5xpmnJMDC")
                .roles("ADMIN", "USER").build(),
            User.withUsername("chef")
                .password("$2a$10$PJHhHpbPK9NTlsEfJ.1cMuJOAjLsWl9HoSXKMuWZco8n/hIWOAZza")
                .roles("USER").build(),
            User.withUsername("usuario")
                .password("$2a$10$1stmIo2A6rnhlatVdaoFReRHPyjAjyRYvnIvBoGhpULo75x7EXD8O")
                .roles("USER").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
