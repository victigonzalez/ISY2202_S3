# Sabores del Mundo – ISY2202 Semana 3

Sitio de recetas desarrollado para la actividad sumativa de la semana 3 de **Seguridad y Calidad en el
Desarrollo de Software (ISY2202)**, Duoc UC. Incluye el análisis de seguridad con **ZAP 2.17.0** y la
corrección de las vulnerabilidades según **OWASP Top 10**.

**Autor:** Victor Gonzalez Arenas · Ingeniería en Desarrollo de Software

## Estructura

| Carpeta | Contenido |
|---|---|
| `frontend-recetas/` | Spring Boot 4.1.2 + Spring Security + Thymeleaf. `http://localhost:8080/recetas` |
| `backend-recetas/` | API REST Spring Boot + Spring Data JPA + Oracle Autonomous Database. `https://localhost:8443/api/recetas` |
| `backend-recetas/db/` | `crear_usuario.sql` (usuario con mínimo privilegio) y `script_bd.sql` (tablas y datos) |
| `reportes_zap/` | Reportes de ZAP antes y después de las correcciones |

Cada proyecto tiene su propio README con las instrucciones para ejecutarlo.

## Funcionalidades

| Página | Acceso |
|---|---|
| Inicio: recetas recientes y populares, banners comerciales y acceso al login | Pública |
| Búsqueda por nombre, tipo de cocina, ingrediente, país y dificultad | Pública |
| Detalle de receta: ingredientes, pasos, tiempos, dificultad y fotografía | Privada |

Usuarios de prueba: `admin / Admin#2026`, `chef / Chef#2026`, `usuario / Usuario#2026`.

## Seguridad aplicada (OWASP Top 10)

| Vulnerabilidad detectada por ZAP | Riesgo | Corrección |
|---|---|---|
| Credenciales de autenticación capturadas (backend) | Alto | API solo por HTTPS (TLS) + HSTS |
| Método de autenticación débil (backend) | Medio | HTTPS + respuestas de error en JSON |
| Cabecera Content Security Policy no configurada (frontend) | Medio | CSP, Referrer-Policy y Permissions-Policy con Spring Security |
| Cookie sin atributo SameSite (frontend) | Bajo | Cookie de sesión `HttpOnly` + `SameSite=Strict` |
| XSS potencial (frontend) | Informativo | Escape de Thymeleaf + validación por lista blanca |

Además: contraseñas con BCrypt, consultas JPQL parametrizadas, denegación por defecto en la API,
token CSRF, usuario de base de datos con mínimo privilegio y credenciales fuera del código.

## Pruebas

```bash
cd frontend-recetas && mvn test   # 10 pruebas
cd backend-recetas  && mvn test   # 18 pruebas
```

> Las credenciales de la base de datos, la wallet de Oracle y el certificado HTTPS **no** están en el
> repositorio. Ver `backend-recetas/config/application.properties.example`.
