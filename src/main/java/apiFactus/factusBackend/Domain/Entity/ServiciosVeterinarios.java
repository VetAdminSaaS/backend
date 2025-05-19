package apiFactus.factusBackend.Domain.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "servicios")
public class ServiciosVeterinarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private Float precio;

    @Column(nullable = false)
    private Boolean disponible;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
    private List<EmpleadoServicio> empleados;

    private LocalDateTime created_At;
    private LocalDateTime updated_At;

    @Column(name = "coverPath", nullable = false, columnDefinition = "TEXT")
    private String coverPath;
}
