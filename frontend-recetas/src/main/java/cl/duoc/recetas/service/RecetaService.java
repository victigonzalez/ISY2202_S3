package cl.duoc.recetas.service;

import cl.duoc.recetas.model.Receta;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Catálogo de recetas. Para esta entrega el listado y el detalle son código estático.
 */
@Service
public class RecetaService {

    private final List<Receta> recetas = List.of(
        new Receta(1L, "Pastel de choclo",
            "Clásico chileno de verano: pino de carne cubierto con pastelera de choclo gratinada.",
            "Chilena", "Chile", "Media", 40, 45, 6,
            List.of("1 kg de choclo desgranado", "500 g de carne molida", "2 cebollas", "1 pechuga de pollo cocida",
                    "3 huevos duros", "Aceitunas negras", "1 cucharada de azúcar", "Albahaca fresca", "Sal, comino y ají de color"),
            List.of("Sofríe la cebolla picada, agrega la carne y condimenta con comino, sal y ají de color.",
                    "Muele el choclo con la albahaca y cocínalo con un poco de leche hasta que espese.",
                    "En una fuente de greda pon el pino, el pollo desmenuzado, huevo y aceitunas.",
                    "Cubre con la pastelera, espolvorea azúcar y gratina en horno a 200 °C por 25 minutos."),
            "pastel-de-choclo.svg", LocalDate.of(2026, 10, 30), 1520),

        new Receta(2L, "Empanadas de pino",
            "Empanadas horneadas rellenas de pino de carne, huevo, aceituna y pasas.",
            "Chilena", "Chile", "Media", 60, 30, 12,
            List.of("1 kg de harina", "200 g de manteca", "500 g de carne picada", "3 cebollas",
                    "2 huevos duros", "Aceitunas", "Pasas", "Comino, pimentón y sal"),
            List.of("Prepara el pino el día anterior: cebolla, carne y condimentos cocidos lentamente.",
                    "Haz la masa con harina, manteca derretida, agua tibia con sal y amasa hasta que esté lisa.",
                    "Estira discos, rellena con pino, huevo, aceituna y pasas, y cierra con el repulgue.",
                    "Pinta con huevo y hornea a 200 °C por 25–30 minutos."),
            "empanadas.svg", LocalDate.of(2026, 9, 18), 2310),

        new Receta(3L, "Tacos al pastor",
            "Tacos de cerdo adobado con achiote y piña, servidos en tortilla de maíz.",
            "Mexicana", "México", "Media", 30, 25, 4,
            List.of("800 g de lomo de cerdo", "3 chiles guajillo", "50 g de pasta de achiote", "1/4 de piña",
                    "12 tortillas de maíz", "Cebolla y cilantro", "Limón"),
            List.of("Hidrata los chiles y licúalos con achiote, vinagre, ajo y especias.",
                    "Marina el cerdo en láminas finas al menos 2 horas.",
                    "Cocina el cerdo en sartén muy caliente junto con trozos de piña.",
                    "Sirve en tortillas calientes con cebolla, cilantro y limón."),
            "tacos.svg", LocalDate.of(2026, 11, 2), 1840),

        new Receta(4L, "Guacamole",
            "Salsa fresca de palta, ideal para acompañar totopos o tacos.",
            "Mexicana", "México", "Fácil", 10, 0, 4,
            List.of("3 paltas maduras", "1 tomate", "1/2 cebolla morada", "1 chile serrano",
                    "Cilantro", "1 limón", "Sal"),
            List.of("Pica finamente tomate, cebolla, chile y cilantro.",
                    "Muele las paltas con un tenedor dejando algunos trozos.",
                    "Mezcla todo, agrega jugo de limón y sal a gusto."),
            "guacamole.svg", LocalDate.of(2026, 10, 12), 980),

        new Receta(5L, "Risotto de champiñones",
            "Arroz arborio cremoso con champiñones salteados y queso parmesano.",
            "Italiana", "Italia", "Media", 15, 30, 4,
            List.of("320 g de arroz arborio", "300 g de champiñones", "1 litro de caldo de verduras",
                    "1 cebolla", "100 ml de vino blanco", "50 g de mantequilla", "60 g de parmesano"),
            List.of("Saltea los champiñones y resérvalos.",
                    "Sofríe la cebolla, agrega el arroz y nácaralo 2 minutos; desglasa con el vino.",
                    "Agrega caldo caliente de a poco, revolviendo, durante unos 18 minutos.",
                    "Incorpora champiñones, mantequilla y parmesano; deja reposar 2 minutos."),
            "risotto.svg", LocalDate.of(2026, 11, 5), 760),

        new Receta(6L, "Lasaña boloñesa",
            "Capas de pasta, salsa boloñesa y bechamel gratinadas al horno.",
            "Italiana", "Italia", "Difícil", 60, 50, 8,
            List.of("12 láminas de lasaña", "600 g de carne molida", "800 g de tomate triturado",
                    "1 zanahoria", "1 cebolla", "1 litro de leche", "60 g de harina", "200 g de queso mozzarella"),
            List.of("Prepara la boloñesa cocinando el sofrito con la carne y el tomate por 40 minutos.",
                    "Haz la bechamel con mantequilla, harina y leche, sin dejar de batir.",
                    "Arma capas: salsa, pasta, boloñesa, bechamel; repite y termina con queso.",
                    "Hornea a 180 °C por 40 minutos y deja reposar 10 antes de cortar."),
            "lasana.svg", LocalDate.of(2026, 8, 22), 2050),

        new Receta(7L, "Ceviche peruano",
            "Pescado blanco curado en jugo de limón con cebolla morada y ají limo.",
            "Peruana", "Perú", "Fácil", 25, 0, 4,
            List.of("600 g de corvina", "15 limones", "1 cebolla morada", "1 ají limo",
                    "Cilantro", "Camote cocido", "Choclo peruano", "Sal"),
            List.of("Corta el pescado en cubos y sazónalo con sal.",
                    "Agrega ají picado y el jugo de limón recién exprimido; mezcla 2 minutos.",
                    "Incorpora la cebolla en pluma y el cilantro.",
                    "Sirve de inmediato con camote y choclo."),
            "ceviche.svg", LocalDate.of(2026, 10, 25), 1310),

        new Receta(8L, "Ramen de cerdo",
            "Caldo intenso de cerdo con fideos, huevo marinado y chashu.",
            "Japonesa", "Japón", "Difícil", 45, 240, 4,
            List.of("1 kg de huesos de cerdo", "500 g de panceta", "4 porciones de fideos ramen",
                    "4 huevos", "Salsa de soya", "Mirin", "Cebollín", "Alga nori"),
            List.of("Blanquea los huesos y cuécelos a fuego fuerte por 4 horas para el caldo.",
                    "Enrolla la panceta y cuécela en soya, mirin y azúcar (chashu).",
                    "Cuece los huevos 6 ½ minutos y márinalos en la salsa del chashu.",
                    "Sirve el caldo sobre los fideos con chashu, huevo, cebollín y nori."),
            "ramen.svg", LocalDate.of(2026, 9, 30), 1675),

        new Receta(9L, "Paella valenciana",
            "Arroz con pollo, conejo, judías verdes y azafrán cocinado en paellera.",
            "Española", "España", "Difícil", 30, 45, 6,
            List.of("400 g de arroz bomba", "500 g de pollo", "300 g de conejo", "200 g de judías verdes",
                    "1 tomate rallado", "Azafrán", "Pimentón dulce", "Aceite de oliva"),
            List.of("Dora el pollo y el conejo en aceite de oliva en la paellera.",
                    "Agrega las verduras, el tomate y el pimentón; sofríe unos minutos.",
                    "Añade agua y azafrán, cuece 20 minutos y reparte el arroz.",
                    "Cocina 18 minutos sin revolver y deja reposar tapado 5 minutos."),
            "paella.svg", LocalDate.of(2026, 10, 5), 1190)
    );

    public List<Receta> listarTodas() {
        return recetas;
    }

    public Optional<Receta> buscarPorId(Long id) {
        return recetas.stream().filter(r -> r.id().equals(id)).findFirst();
    }

    public List<Receta> masRecientes(int cantidad) {
        return recetas.stream()
                .sorted(Comparator.comparing(Receta::fechaPublicacion).reversed())
                .limit(cantidad).toList();
    }

    public List<Receta> masPopulares(int cantidad) {
        return recetas.stream()
                .sorted(Comparator.comparingInt(Receta::visitas).reversed())
                .limit(cantidad).toList();
    }

    /** Filtra por cualquier combinación de criterios; los vacíos se ignoran. */
    public List<Receta> buscar(String nombre, String tipoCocina, String ingrediente,
                               String pais, String dificultad) {
        return recetas.stream()
                .filter(r -> contiene(r.nombre(), nombre))
                .filter(r -> igual(r.tipoCocina(), tipoCocina))
                .filter(r -> vacio(ingrediente)
                        || r.ingredientes().stream().anyMatch(i -> contiene(i, ingrediente)))
                .filter(r -> igual(r.pais(), pais))
                .filter(r -> igual(r.dificultad(), dificultad))
                .toList();
    }

    public List<String> tiposDeCocina() {
        return recetas.stream().map(Receta::tipoCocina).distinct().sorted().toList();
    }

    public List<String> paises() {
        return recetas.stream().map(Receta::pais).distinct().sorted().toList();
    }

    public List<String> dificultades() {
        return List.of("Fácil", "Media", "Difícil");
    }

    // ---- utilidades de comparación (sin mayúsculas ni tildes) ----

    private static boolean vacio(String s) {
        return s == null || s.isBlank();
    }

    private static boolean contiene(String texto, String filtro) {
        return vacio(filtro) || normalizar(texto).contains(normalizar(filtro));
    }

    private static boolean igual(String texto, String filtro) {
        return vacio(filtro) || normalizar(texto).equals(normalizar(filtro));
    }

    private static String normalizar(String s) {
        return Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }
}
