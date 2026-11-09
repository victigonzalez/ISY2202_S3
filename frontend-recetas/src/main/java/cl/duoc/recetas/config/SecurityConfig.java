package cl.duoc.recetas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Configuración de Spring Security.
 *
 * Páginas PÚBLICAS : inicio ("/"), búsqueda ("/buscar"), login y recursos estáticos.
 * Páginas PRIVADAS : todo lo demás, en particular el detalle de receta ("/receta/{id}").
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/buscar", "/login", "/error",
                                 "/css/**", "/img/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", false)   // si venía de una página privada, vuelve a ella
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            // Cabeceras de seguridad (OWASP A05 - Configuración de seguridad incorrecta)
            .headers(h -> h
                // CSP: solo se cargan recursos del propio sitio; sin scripts externos ni inline,
                // sin plugins, y el sitio no puede incrustarse en iframes de terceros.
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self'; "
                  + "font-src 'self'; object-src 'none'; base-uri 'self'; form-action 'self'; "
                  + "frame-ancestors 'none'"))
                .referrerPolicy(r -> r.policy(
                    ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy",
                    "camera=(), microphone=(), geolocation=(), payment=()"))
            );
        return http.build();
    }

    /**
     * Tres usuarios en memoria. Las contraseñas se guardan como hash BCrypt,
     * nunca en texto plano dentro del código.
     *   admin   / Admin#2026
     *   chef    / Chef#2026
     *   usuario / Usuario#2026
     */
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
