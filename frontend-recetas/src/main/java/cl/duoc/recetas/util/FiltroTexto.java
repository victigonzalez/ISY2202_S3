package cl.duoc.recetas.util;

/**
 * Validación de entradas de búsqueda (lista blanca).
 *
 * - Solo se permiten letras (incluidas tildes y ñ), números, espacios y guion.
 *   Se eliminan caracteres usados en ataques XSS / inyección (< > " ' ; ( ) etc.).
 * - Largo máximo de 60 caracteres (igual que el maxlength del formulario).
 * - Texto vacío se convierte en null para que el filtro se ignore.
 *
 * Es una capa adicional: Thymeleaf (th:text / th:value) ya escapa la salida.
 */
public final class FiltroTexto {

    public static final int LARGO_MAXIMO = 60;

    private FiltroTexto() { }

    public static String limpiar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.replaceAll("[^\\p{L}\\p{N} \\-]", "")
                             .replaceAll("\\s+", " ")
                             .trim();
        if (limpio.length() > LARGO_MAXIMO) {
            limpio = limpio.substring(0, LARGO_MAXIMO).trim();
        }
        return limpio.isEmpty() ? null : limpio;
    }
}
