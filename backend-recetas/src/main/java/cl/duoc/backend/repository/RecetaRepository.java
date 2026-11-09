package cl.duoc.backend.repository;

import cl.duoc.backend.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long> {

    List<Receta> findAllByOrderByFechaPublicacionDesc();

    /**
     * Búsqueda con filtros opcionales (los null se ignoran).
     * Consulta parametrizada: los valores nunca se concatenan al SQL (previene inyección SQL).
     */
    @Query("""
           SELECT r FROM Receta r
           WHERE (:nombre IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
             AND (:tipoCocina IS NULL OR LOWER(r.tipoCocina) = LOWER(:tipoCocina))
             AND (:pais IS NULL OR LOWER(r.pais) = LOWER(:pais))
             AND (:dificultad IS NULL OR LOWER(r.dificultad) = LOWER(:dificultad))
             AND (:ingrediente IS NULL OR EXISTS (
                    SELECT i FROM Ingrediente i
                    WHERE i.receta = r
                      AND LOWER(i.descripcion) LIKE LOWER(CONCAT('%', :ingrediente, '%'))))
           ORDER BY r.nombre
           """)
    List<Receta> buscar(@Param("nombre") String nombre,
                        @Param("tipoCocina") String tipoCocina,
                        @Param("ingrediente") String ingrediente,
                        @Param("pais") String pais,
                        @Param("dificultad") String dificultad);
}
