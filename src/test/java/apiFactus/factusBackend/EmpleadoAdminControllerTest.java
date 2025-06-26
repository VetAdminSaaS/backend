package apiFactus.factusBackend;

import apiFactus.factusBackend.Dto.*;
import apiFactus.factusBackend.Service.EmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmpleadoAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarEmpleados() throws Exception {
        EmpleadosDetailsDTO dto = new EmpleadosDetailsDTO();
        dto.setId(1L);
        dto.setNombre("Juan");

        when(empleadoService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    void testCrearEmpleado() throws Exception {
        EmpleadoRegistrationDTO request = new EmpleadoRegistrationDTO();
        request.setEmail("empleado@correo.com");
        request.setPassword("12345678");

        EmpleadoRegistrationDTO response = new EmpleadoRegistrationDTO();
        response.setEmail("empleado@correo.com");

        when(empleadoService.crearEmpleado(any())).thenReturn(response);

        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("empleado@correo.com"));
    }

    @Test
    void testObtenerEmpleadoPorId() throws Exception {
        EmpleadosDetailsDTO dto = new EmpleadosDetailsDTO();
        dto.setId(2L);
        dto.setNombre("Lucía");

        when(empleadoService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/empleados/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Lucía"));
    }

    @Test
    void testActualizarEmpleado() throws Exception {
        EmpleadosDetailsDTO request = new EmpleadosDetailsDTO();
        request.setNombre("Carlos");

        EmpleadosDetailsDTO response = new EmpleadosDetailsDTO();
        response.setId(3L);
        response.setNombre("Carlos");

        when(empleadoService.update(3L, request)).thenReturn(response);

        mockMvc.perform(put("/empleados/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testEliminarEmpleado() throws Exception {
        doNothing().when(empleadoService).delete(4L);

        mockMvc.perform(delete("/empleados/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testRegistroEmpleadoVeterinario() throws Exception {
        EmpleadoRegistrationDTO request = new EmpleadoRegistrationDTO();
        request.setEmail("vet@correo.com");
        request.setPassword("12345678");

        EmpleadoProfileDTO response = new EmpleadoProfileDTO();
        response.setEmail("vet@correo.com");

        when(empleadoService.registroEmpleadoVeterinario(any())).thenReturn(response);

        mockMvc.perform(post("/empleados/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("vet@correo.com"));
    }

    @Test
    void testCompletarRegistroEmpleado() throws Exception {
        EmpleadosDTO request = new EmpleadosDTO();
        request.setNombre("Sandra");

        EmpleadosDTO response = new EmpleadosDTO();
        response.setId(6L);
        response.setNombre("Sandra");

        when(empleadoService.completarRegistro(6L, request)).thenReturn(response);

        mockMvc.perform(put("/empleados/6/completar-registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6L))
                .andExpect(jsonPath("$.nombre").value("Sandra"));
    }
}
