package apiFactus.factusBackend.Dto;

import lombok.Data;

@Data
public class EmailAsesoriaRequest {
    private String nombre;
    private String email;
    private String telefono;
    private String empresa;
    private String cargo;
    private String mentor;
    private String nombreAsesoria;
    private String categoria;
    private String hora;
    private String fecha;
    private String modalidad;
    private final String duracion = "60 minutos";

    // Getters y Setters
}
