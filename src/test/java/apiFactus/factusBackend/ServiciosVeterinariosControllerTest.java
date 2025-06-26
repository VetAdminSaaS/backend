package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.ServiciosVeterinariosDTO;
import apiFactus.factusBackend.Service.ServiciosVeterinariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiciosVeterinariosController.class)
class ServiciosVeterinariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiciosVeterinariosService serviciosVeterinariosService;

    @Autowired
    private ObjectMapper objectMapper;

    private ServiciosVeterinariosDTO servicio1;
    private ServiciosVeterinariosDTO servicio2;

    @BeforeEach
    void setUp() {
        servicio1 = new ServiciosVeterinariosDTO();
        servicio1.setId(1L);
        servicio1.setNombre("Consulta general");
        servicio1.setDescripcion("Evaluación general del estado del animal");
        servicio1.setPrecio(50.0f);
        servicio1.setDisponible(true);

        servicio2 = new ServiciosVeterinariosDTO();
        servicio2.setId(2L);
        servicio2.setNombre("Vacunación");
        servicio2.setDescripcion("Aplicación de vacunas");
        servicio2.setPrecio(75.0f);
        servicio2.setDisponible(true);
    }

    @Test
    void getAllServiciosTest() throws Exception {
        List<ServiciosVeterinariosDTO> lista = Arrays.asList(servicio1, servicio2);
        when(serviciosVeterinariosService.getAllServiciosVeterinarios()).thenReturn(lista);

        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllServiciosPageTest() throws Exception {
        Page<ServiciosVeterinariosDTO> page = new PageImpl<>(List.of(servicio1));
        when(serviciosVeterinariosService.getAllServiciosVeterinarios(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/servicios/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void getServicioByIdTest() throws Exception {
        when(serviciosVeterinariosService.findbyId(1L)).thenReturn(servicio1);

        mockMvc.perform(get("/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Consulta general"));
    }

    @Test
    void crearServicioTest() throws Exception {
        when(serviciosVeterinariosService.crearServicioVeterinario(any())).thenReturn(servicio1);

        mockMvc.perform(post("/servicios/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicio1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Consulta general"));
    }

    @Test
    void updateServiciosTest() throws Exception {
        when(serviciosVeterinariosService.updateServicioVeterinario(eq(1L), any())).thenReturn(servicio1);

        mockMvc.perform(put("/servicios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(servicio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Consulta general"));
    }

    @Test
    void deleteServiciosTest() throws Exception {
        doNothing().when(serviciosVeterinariosService).delete(1L);

        mockMvc.perform(delete("/servicios/1"))
                .andExpect(status().isNoContent());
    }
}
