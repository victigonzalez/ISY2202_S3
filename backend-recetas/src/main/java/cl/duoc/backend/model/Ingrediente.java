package cl.duoc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta;

    private Integer orden;
    private String descripcion;

    protected Ingrediente() { }

    public Long getId() { return id; }
    public Integer getOrden() { return orden; }
    public String getDescripcion() { return descripcion; }
}
