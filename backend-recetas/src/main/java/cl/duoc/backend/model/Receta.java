package cl.duoc.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receta")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String tipoCocina;
    private String pais;
    private String dificultad;
    private Integer tiempoPreparacion;
    private Integer tiempoCoccion;
    private Integer porciones;
    private String imagen;
    private LocalDate fechaPublicacion;
    private Integer visitas;

    @OneToMany(mappedBy = "receta", fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @OneToMany(mappedBy = "receta", fetch = FetchType.LAZY)
    @OrderBy("numero ASC")
    private List<Paso> pasos = new ArrayList<>();

    protected Receta() { }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getTipoCocina() { return tipoCocina; }
    public String getPais() { return pais; }
    public String getDificultad() { return dificultad; }
    public Integer getTiempoPreparacion() { return tiempoPreparacion; }
    public Integer getTiempoCoccion() { return tiempoCoccion; }
    public Integer getPorciones() { return porciones; }
    public String getImagen() { return imagen; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public Integer getVisitas() { return visitas; }
    public List<Ingrediente> getIngredientes() { return ingredientes; }
    public List<Paso> getPasos() { return pasos; }
}
