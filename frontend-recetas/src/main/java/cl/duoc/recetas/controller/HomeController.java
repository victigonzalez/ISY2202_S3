package cl.duoc.recetas.controller;

import cl.duoc.recetas.service.RecetaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** [Pública] Página de inicio: recetas recientes, populares y banners comerciales. */
@Controller
public class HomeController {

    private final RecetaService recetaService;

    public HomeController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("recientes", recetaService.masRecientes(4));
        model.addAttribute("populares", recetaService.masPopulares(4));
        return "index";
    }
}
