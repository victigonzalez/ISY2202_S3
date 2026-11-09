package cl.duoc.backend.dto;

import cl.duoc.backend.model.Ingrediente;
import cl.duoc.backend.model.Paso;
import cl.duoc.backend.model.Receta;

import java.time.LocalDate;
import java.util.List;

/** Detalle completo de una receta (solo usuarios autenticados). */
public record RecetaDetalleDTO(
        Long id,
        String nombre,
        String descripcion,
        String tipoCocina,
        String pais,
        String dificultad,
        int tiempoPreparacion,
        int tiempoCoccion,
        int porciones,
        String imagen,
        LocalDate fechaPublicacion,
        List<String> ingredientes,
        List<String> pasos
) {
    public static RecetaDetalleDTO desde(Receta r) {
        return new RecetaDetalleDTO(r.getId(), r.getNombre(), r.getDescripcion(), r.getTipoCocina(),
                r.getPais(), r.getDificultad(), r.getTiempoPreparacion(), r.getTiempoCoccion(),
                r.getPorciones(), r.getImagen(), r.getFechaPublicacion(),
                r.getIngredientes().stream().map(Ingrediente::getDescripcion).toList(),
                r.getPasos().stream().map(Paso::getDescripcion).toList());
    }
}
