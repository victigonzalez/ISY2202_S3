package cl.duoc.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba la API contra H2 cargado con db/script_bd.sql.
 */
@SpringBootTest
@AutoConfigureMockMvc
// Fuerza H2 aunque exista config/application.properties con la conexión a Oracle
// (@TestPropertySource tiene prioridad sobre ese archivo).
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:recetas;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver"
})
class RecetaApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void listadoEsPublicoYTraeLas9Recetas() throws Exception {
        mvc.perform(get("/api/recetas"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(9)))
           .andExpect(jsonPath("$[0].nombre", is("Risotto de champiñones")));   // la más reciente
    }

    @Test
    void busquedaPorPaisEsPublica() throws Exception {
        mvc.perform(get("/api/recetas/buscar").param("pais", "chile"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void busquedaPorNombreParcial() throws Exception {
        mvc.perform(get("/api/recetas/buscar").param("nombre", "PASTOR"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].nombre", is("Tacos al pastor")));
    }

    @Test
    void busquedaPorIngrediente() throws Exception {
        mvc.perform(get("/api/recetas/buscar").param("ingrediente", "palta"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].nombre", is("Guacamole")));
    }

    @Test
    void busquedaCombinada() throws Exception {
        mvc.perform(get("/api/recetas/buscar")
                .param("tipoCocina", "Italiana").param("dificultad", "Difícil"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].nombre", is("Lasaña boloñesa")));
    }

    @Test
    void busquedaConIntentoDeInyeccionSqlNoDevuelveNada() throws Exception {
        mvc.perform(get("/api/recetas/buscar").param("nombre", "' OR '1'='1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void detalleSinCredencialesDevuelve401EnJson() throws Exception {
        mvc.perform(get("/api/recetas/1"))
           .andExpect(status().isUnauthorized())
           .andExpect(header().string("WWW-Authenticate", "Basic realm=\"recetas-api\""))
           .andExpect(content().contentTypeCompatibleWith("application/json"))
           .andExpect(jsonPath("$.status", is(401)));
    }

    @Test
    void respuestasIncluyenCabecerasDeSeguridad() throws Exception {
        mvc.perform(get("/api/recetas"))
           .andExpect(header().string("Content-Security-Policy", "default-src 'none'; frame-ancestors 'none'"))
           .andExpect(header().string("Referrer-Policy", "no-referrer"))
           .andExpect(header().string("X-Content-Type-Options", "nosniff"))
           .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    void comodinesDelLikeNoFuncionanComoComodines() throws Exception {
        // Sin validación, "_" en un LIKE coincide con cualquier carácter y "pas_el" encontraría "Pastel".
        // Con la lista blanca, "_" se elimina ("pasel") y no hay coincidencias.
        mvc.perform(get("/api/recetas/buscar").param("nombre", "pas_el"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void detalleConCredencialesDevuelveIngredientesYPasos() throws Exception {
        mvc.perform(get("/api/recetas/1").with(httpBasic("chef", "Chef#2026")))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.nombre", is("Pastel de choclo")))
           .andExpect(jsonPath("$.ingredientes", hasSize(9)))
           .andExpect(jsonPath("$.pasos", hasSize(4)));
    }

    @Test
    void detalleConClaveIncorrectaDevuelve401() throws Exception {
        mvc.perform(get("/api/recetas/1").with(httpBasic("chef", "mala")))
           .andExpect(status().isUnauthorized());
    }

    @Test
    void recetaInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/api/recetas/999").with(httpBasic("chef", "Chef#2026")))
           .andExpect(status().isNotFound());
    }

    @Test
    void metodosNoPermitidosEstanBloqueados() throws Exception {
        mvc.perform(delete("/api/recetas/1").with(httpBasic("admin", "Admin#2026")))
           .andExpect(status().isForbidden());
    }
}
