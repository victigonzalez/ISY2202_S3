package cl.duoc.recetas.controller;

import cl.duoc.recetas.model.Receta;
import cl.duoc.recetas.service.RecetaService;
import cl.duoc.recetas.util.FiltroTexto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    /** [Pública] Búsqueda por nombre, tipo de cocina, ingrediente, país y dificultad. */
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String tipoCocina,
                         @RequestParam(required = false) String ingrediente,
                         @RequestParam(required = false) String pais,
                         @RequestParam(required = false) String dificultad,
                         Model model) {

        // Validación de entradas por lista blanca (OWASP A03 - Inyección)
        nombre = FiltroTexto.limpiar(nombre);
        tipoCocina = FiltroTexto.limpiar(tipoCocina);
        ingrediente = FiltroTexto.limpiar(ingrediente);
        pais = FiltroTexto.limpiar(pais);
        dificultad = FiltroTexto.limpiar(dificultad);

        List<Receta> resultados = recetaService.buscar(nombre, tipoCocina, ingrediente, pais, dificultad);

        model.addAttribute("resultados", resultados);
        model.addAttribute("tiposCocina", recetaService.tiposDeCocina());
        model.addAttribute("paises", recetaService.paises());
        model.addAttribute("dificultades", recetaService.dificultades());
        // devolvemos los filtros para mantenerlos en el formulario
        model.addAttribute("nombre", nombre);
        model.addAttribute("tipoCocina", tipoCocina);
        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("pais", pais);
        model.addAttribute("dificultad", dificultad);
        return "buscar";
    }

    /** [Privada] Detalle de una receta: solo usuarios autenticados (ver SecurityConfig). */
    @GetMapping("/receta/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Receta receta = recetaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta no encontrada"));
        model.addAttribute("receta", receta);
        return "detalle";
    }
}
