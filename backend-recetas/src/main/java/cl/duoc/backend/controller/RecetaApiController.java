package cl.duoc.backend.controller;

import cl.duoc.backend.dto.RecetaDetalleDTO;
import cl.duoc.backend.dto.RecetaResumenDTO;
import cl.duoc.backend.service.RecetaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaApiController {

    private final RecetaService service;

    public RecetaApiController(RecetaService service) {
        this.service = service;
    }

    /** [Pública] Listado de recetas, más recientes primero. */
    @GetMapping
    public List<RecetaResumenDTO> listar() {
        return service.listar();
    }

    /** [Pública] Búsqueda por nombre, tipo de cocina, ingrediente, país y dificultad. */
    @GetMapping("/buscar")
    public List<RecetaResumenDTO> buscar(@RequestParam(required = false) String nombre,
                                         @RequestParam(required = false) String tipoCocina,
                                         @RequestParam(required = false) String ingrediente,
                                         @RequestParam(required = false) String pais,
                                         @RequestParam(required = false) String dificultad) {
        return service.buscar(nombre, tipoCocina, ingrediente, pais, dificultad);
    }

    /** [Privada] Detalle con ingredientes y pasos. Requiere autenticación HTTP Basic. */
    @GetMapping("/{id}")
    public RecetaDetalleDTO detalle(@PathVariable Long id) {
        return service.detalle(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta no encontrada"));
    }
}
