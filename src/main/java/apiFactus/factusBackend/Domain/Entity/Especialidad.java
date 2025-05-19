package apiFactus.factusBackend.Domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_At", nullable = true)
    private LocalDateTime updatedAt;
    @Version
    private int version;
    @JsonIgnore
    @ManyToMany(mappedBy = "especialidades")
    private List<EmpleadoVeterinario> empleados;
}
