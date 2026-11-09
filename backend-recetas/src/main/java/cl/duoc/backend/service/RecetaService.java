package cl.duoc.backend.service;

import cl.duoc.backend.dto.RecetaDetalleDTO;
import cl.duoc.backend.dto.RecetaResumenDTO;
import cl.duoc.backend.repository.RecetaRepository;
import cl.duoc.backend.util.FiltroTexto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RecetaService {

    private final RecetaRepository repo;

    public RecetaService(RecetaRepository repo) {
        this.repo = repo;
    }

    public List<RecetaResumenDTO> listar() {
        return repo.findAllByOrderByFechaPublicacionDesc().stream().map(RecetaResumenDTO::desde).toList();
    }

    public List<RecetaResumenDTO> buscar(String nombre, String tipoCocina, String ingrediente,
                                         String pais, String dificultad) {
        // Validación por lista blanca antes de llegar a la consulta (que además es parametrizada)
        return repo.buscar(FiltroTexto.limpiar(nombre), FiltroTexto.limpiar(tipoCocina),
                           FiltroTexto.limpiar(ingrediente), FiltroTexto.limpiar(pais),
                           FiltroTexto.limpiar(dificultad))
                   .stream().map(RecetaResumenDTO::desde).toList();
    }

    public Optional<RecetaDetalleDTO> detalle(Long id) {
        return repo.findById(id).map(RecetaDetalleDTO::desde);
    }
}
