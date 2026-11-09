package cl.duoc.backend.dto;

import cl.duoc.backend.model.Receta;

import java.time.LocalDate;

/** Datos públicos de una receta (listado y búsqueda). */
public record RecetaResumenDTO(
        Long id,
        String nombre,
        String descripcion,
        String tipoCocina,
        String pais,
        String dificultad,
        int tiempoTotal,
        String imagen,
        LocalDate fechaPublicacion,
        int visitas
) {
    public static RecetaResumenDTO desde(Receta r) {
        return new RecetaResumenDTO(r.getId(), r.getNombre(), r.getDescripcion(), r.getTipoCocina(),
                r.getPais(), r.getDificultad(), r.getTiempoPreparacion() + r.getTiempoCoccion(),
                r.getImagen(), r.getFechaPublicacion(), r.getVisitas());
    }
}
