package cl.duoc.backend;

import cl.duoc.backend.util.FiltroTexto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Prueba la validación por lista blanca de los parámetros de búsqueda. */
class FiltroTextoTest {

    @Test
    void eliminaCaracteresDeAtaqueXss() {
        assertEquals("scriptalert1script", FiltroTexto.limpiar("\"><script>alert(1)</script>"));
    }

    @Test
    void eliminaCaracteresDeInyeccionSql() {
        assertEquals("OR 11", FiltroTexto.limpiar("' OR '1'='1"));
        assertEquals("pasel", FiltroTexto.limpiar("pas_el"));
        assertNull(FiltroTexto.limpiar("%"));
    }

    @Test
    void conservaTildesEnesYGuion() {
        assertEquals("Japón", FiltroTexto.limpiar("  Japón "));
        assertEquals("ñoquis al pesto-genovés", FiltroTexto.limpiar("ñoquis al pesto-genovés"));
    }

    @Test
    void limitaElLargo() {
        assertEquals(FiltroTexto.LARGO_MAXIMO, FiltroTexto.limpiar("a".repeat(500)).length());
    }

    @Test
    void vacioEsNulo() {
        assertNull(FiltroTexto.limpiar(null));
        assertNull(FiltroTexto.limpiar("   "));
    }
}
