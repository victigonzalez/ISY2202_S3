package cl.duoc.recetas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Levanta el Tomcat real (no simulado) para verificar los atributos de la cookie de sesión,
 * que los agrega el servidor según application.properties.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CookieSesionTest {

    @LocalServerPort
    int puerto;

    @Test
    void cookieDeSesionTieneHttpOnlyYSameSiteStrict() throws Exception {
        HttpClient cliente = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
        HttpRequest peticion = HttpRequest.newBuilder(
                URI.create("http://localhost:" + puerto + "/recetas/receta/1")).GET().build();

        HttpResponse<Void> respuesta = cliente.send(peticion, HttpResponse.BodyHandlers.discarding());

        assertEquals(302, respuesta.statusCode());   // página privada -> redirige al login
        String cookie = respuesta.headers().firstValue("Set-Cookie").orElseThrow();
        assertTrue(cookie.startsWith("JSESSIONID="), cookie);
        assertTrue(cookie.contains("HttpOnly"), cookie);
        assertTrue(cookie.contains("SameSite=Strict"), cookie);
    }
}
