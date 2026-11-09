# Frontend – Sabores del Mundo (ISY2202, Semana 3)

Spring Boot 4.1 + Spring Security + Thymeleaf. Java 17 o superior, Maven.

## Ejecutar

```bash
mvn spring-boot:run
```

Abrir: http://localhost:8080/recetas

## Usuarios de prueba

| Usuario | Contraseña   | Rol         |
|---------|--------------|-------------|
| admin   | Admin#2026   | ADMIN, USER |
| chef    | Chef#2026    | USER        |
| usuario | Usuario#2026 | USER        |

## Rutas

| Ruta            | Acceso  | Descripción                                             |
|-----------------|---------|---------------------------------------------------------|
| `/`             | Pública | Inicio: recetas recientes, populares y banners          |
| `/buscar`       | Pública | Búsqueda por nombre, cocina, ingrediente, país, dificultad |
| `/login`        | Pública | Inicio de sesión                                        |
| `/receta/{id}`  | Privada | Detalle de receta (requiere login)                      |

## Pruebas

```bash
mvn test
```

- `SeguridadRutasTest`: rutas públicas y privadas, login válido e inválido, cabeceras de
  seguridad y que la búsqueda no refleje código `<script>`.
- `CookieSesionTest`: la cookie de sesión lleva `HttpOnly` y `SameSite=Strict`.

## Medidas de seguridad (OWASP Top 10)

| Medida | Riesgo que mitiga |
|---|---|
| Rutas públicas/privadas con Spring Security, login propio | Control de acceso (A01) |
| Token CSRF en login y logout (Thymeleaf `th:action`) | CSRF (A01) |
| Cookie de sesión `HttpOnly` + `SameSite=Strict` | Robo de sesión / CSRF (A01) |
| Contraseñas con hash BCrypt | Exposición de credenciales (A02 / A07) |
| Escape automático de Thymeleaf + validación por lista blanca de la búsqueda | XSS / inyección (A03) |
| Content-Security-Policy, X-Frame-Options, X-Content-Type-Options, Referrer-Policy, Permissions-Policy | Configuración de seguridad (A05) |
| Página de error propia, sin trazas | Exposición de información (A05) |

> En producción el sitio debe publicarse detrás de HTTPS y con la cookie `Secure` activada.
