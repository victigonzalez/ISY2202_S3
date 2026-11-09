# Backend – API REST de recetas (ISY2202, Semana 3)

Spring Boot 4.1 + Spring Data JPA + Spring Security (HTTP Basic) + Oracle Autonomous Database.
Java 17 o superior, Maven. Se sirve **solo por HTTPS** en **https://localhost:8443**.

## 1. Preparar la base de datos en Oracle Cloud

1. Se usa la Autonomous Database existente **foro** (Procesamiento de transacciones, 26ai, Always Free).
   Las tablas de recetas quedan aisladas en su propio esquema `RECETAS_APP`, separadas de las del foro.
2. Wallet de la base foro (*Conexión de base de datos* → *Descargar wallet*) descomprimida en `C:/wallets/Wallet_foro`.
   Alias de conexión: `foro_tp` (ver `tnsnames.ora` dentro de la wallet).
3. Conectarse como **ADMIN** (SQL Developer o Database Actions) y ejecutar `db/crear_usuario.sql`,
   reemplazando antes la contraseña de ejemplo.
4. Conectarse como **RECETAS_APP** y ejecutar `db/script_bd.sql` (F5 en SQL Developer).
   Al final debe mostrar 9 recetas, 70 ingredientes y 35 pasos.

> `script_bd.sql` usa `DROP TABLE IF EXISTS` (Oracle 23ai o superior; la base foro es 26ai).

## 2. Generar el certificado HTTPS (una sola vez)

Desde la carpeta `backend-recetas` (keytool viene con el JDK):

```powershell
keytool -genkeypair -alias recetas-api -keyalg RSA -keysize 2048 -storetype PKCS12 `
  -keystore config/recetas-api.p12 -validity 365 `
  -dname "CN=localhost, OU=ISY2202, O=Duoc UC, C=CL" -ext "SAN=dns:localhost,ip:127.0.0.1" `
  -storepass TU_CLAVE_DEL_CERTIFICADO
```

Es un certificado **autofirmado** (válido para el entorno local). El archivo `.p12` contiene la clave
privada y está en `.gitignore`.

## 3. Configurar credenciales (fuera del código)

Copiar `config/application.properties.example` como `config/application.properties` y completar
la clave de la base de datos y la del certificado. Ese archivo está en `.gitignore`.
También se pueden usar variables de entorno: `DB_TNS_ALIAS`, `DB_WALLET_PATH`, `DB_USER`,
`DB_PASSWORD`, `SSL_KEYSTORE_PASSWORD`.

## 4. Ejecutar

```bash
mvn spring-boot:run
```

## Endpoints

| Método | Ruta                    | Acceso                  | Descripción |
|--------|-------------------------|-------------------------|-------------|
| GET    | `/api/recetas`          | Pública                 | Listado (más recientes primero) |
| GET    | `/api/recetas/buscar`   | Pública                 | Filtros: `nombre`, `tipoCocina`, `ingrediente`, `pais`, `dificultad` |
| GET    | `/api/recetas/{id}`     | Privada (HTTP Basic)    | Detalle con ingredientes y pasos |
| otros  | cualquier otra ruta     | Bloqueada (401/403)     | Denegado por defecto |

Todas las respuestas de error son JSON genéricos (`{"status":404,"error":"Not Found"}`), sin trazas
ni la página "Whitelabel" de Spring.

Usuarios: `admin / Admin#2026`, `chef / Chef#2026`, `usuario / Usuario#2026`.

En **PowerShell** usar `curl.exe` (el comando `curl` a secas es un alias de `Invoke-WebRequest`).
`-k` acepta el certificado autofirmado:

```powershell
# Pública
curl.exe -k -i "https://localhost:8443/api/recetas/buscar?pais=Chile"

# Privada sin credenciales -> 401
curl.exe -k -i https://localhost:8443/api/recetas/1

# Privada con credenciales -> 200
curl.exe -k -i -u "chef:Chef#2026" https://localhost:8443/api/recetas/1
```

## Medidas de seguridad (OWASP Top 10)

| Medida | Riesgo que mitiga |
|---|---|
| HTTPS (TLS) + HSTS | Credenciales HTTP Basic expuestas en texto plano (A02 Fallas criptográficas) |
| Detalle protegido con HTTP Basic, resto denegado por defecto | Control de acceso (A01) |
| Contraseñas con hash BCrypt | Exposición de credenciales (A02 / A07) |
| Consultas JPQL parametrizadas + validación por lista blanca | Inyección SQL / comodines LIKE (A03) |
| CSP, X-Frame-Options, X-Content-Type-Options, Referrer-Policy | Configuración de seguridad (A05) |
| Errores JSON genéricos, sin trazas ni cabecera Server | Exposición de información (A05) |
| Credenciales de BD y certificado fuera del código, usuario de BD con mínimo privilegio | Configuración / secretos (A05) |

## Pruebas

```bash
mvn test
```

Las pruebas usan H2 en memoria cargado con el mismo `db/script_bd.sql`, por lo que no necesitan conexión a Oracle Cloud.
