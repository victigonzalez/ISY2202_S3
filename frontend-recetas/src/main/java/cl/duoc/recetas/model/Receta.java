package cl.duoc.recetas.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Receta (datos estáticos para esta entrega).
 */
public record Receta(
        Long id,
        String nombre,
        String descripcion,
        String tipoCocina,
        String pais,
        String dificultad,          // Fácil | Media | Difícil
        int tiempoPreparacion,      // minutos
        int tiempoCoccion,          // minutos
        int porciones,
        List<String> ingredientes,
        List<String> pasos,
        String imagen,              // ruta dentro de /static/img
        LocalDate fechaPublicacion,
        int visitas                 // usado para "más populares"
) {
    public int tiempoTotal() {
        return tiempoPreparacion + tiempoCoccion;
    }
}
