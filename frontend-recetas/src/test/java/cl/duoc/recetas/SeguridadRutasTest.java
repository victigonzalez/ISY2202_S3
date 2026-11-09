package cl.duoc.recetas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Verifica que las rutas públicas y privadas se comporten como pide la actividad.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SeguridadRutasTest {

    @Autowired
    MockMvc mvc;

    @Test
    void inicioEsPublico() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void busquedaEsPublica() throws Exception {
        mvc.perform(get("/buscar").param("pais", "Chile"))
           .andExpect(status().isOk())
           .andExpect(model().attributeExists("resultados"));
    }

    @Test
    void loginEsPublico() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

    @Test
    void detalleSinLoginRedirigeAlLogin() throws Exception {
        mvc.perform(get("/receta/1"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "chef")
    void detalleConLoginEsVisible() throws Exception {
        mvc.perform(get("/receta/1")).andExpect(status().isOk());
    }

    @Test
    void paginasIncluyenCabecerasDeSeguridad() throws Exception {
        mvc.perform(get("/"))
           .andExpect(header().string("Content-Security-Policy", containsString("default-src 'self'")))
           .andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
           .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
           .andExpect(header().exists("Permissions-Policy"))
           .andExpect(header().string("X-Frame-Options", "DENY"))
           .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    void busquedaNoReflejaCodigoMalicioso() throws Exception {
        mvc.perform(get("/buscar").param("nombre", "\"><script>alert(1)</script>"))
           .andExpect(status().isOk())
           .andExpect(content().string(not(containsString("<script>alert(1)</script>"))))
           .andExpect(content().string(not(containsString("\"><script"))));
    }

    @Test
    void loginConUsuarioValido() throws Exception {
        mvc.perform(formLogin("/login").user("chef").password("Chef#2026"))
           .andExpect(authenticated().withUsername("chef"));
    }

    @Test
    void loginConClaveIncorrecta() throws Exception {
        mvc.perform(formLogin("/login").user("chef").password("mala"))
           .andExpect(unauthenticated());
    }
}
