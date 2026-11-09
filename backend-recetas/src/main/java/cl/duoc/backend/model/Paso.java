package cl.duoc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paso")
public class Paso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id")
    private Receta receta;

    private Integer numero;
    private String descripcion;

    protected Paso() { }

    public Long getId() { return id; }
    public Integer getNumero() { return numero; }
    public String getDescripcion() { return descripcion; }
}
