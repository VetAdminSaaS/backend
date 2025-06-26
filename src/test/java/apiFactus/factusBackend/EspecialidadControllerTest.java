package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.EspecialidadDTO;
import apiFactus.factusBackend.Service.EspecialidadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EspecialidadController.class)
@WithMockUser(roles = "ADMIN")
public class EspecialidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EspecialidadService especialidadService;

    @Autowired
    private ObjectMapper objectMapper;

    private EspecialidadDTO especialidad1;
    private EspecialidadDTO especialidad2;

    @BeforeEach
    public void setUp() {
        especialidad1 = new EspecialidadDTO();
        especialidad1.setId(1L);
        especialidad1.setNombre("Dermatología");

        especialidad2 = new EspecialidadDTO();
        especialidad2.setId(2L);
        especialidad2.setNombre("Cardiología");
    }

    @Test
    public void testGetAllEspecialidades() throws Exception {
        List<EspecialidadDTO> lista = Arrays.asList(especialidad1, especialidad2);
        Mockito.when(especialidadService.getAllEspecialidad()).thenReturn(lista);

        mockMvc.perform(get("/especialidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void testGetAllEspecialidadesPage() throws Exception {
        Mockito.when(especialidadService.getAllEspecialidadDTO(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(especialidad1, especialidad2)));

        mockMvc.perform(get("/especialidades/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void testGetEspecialidadById() throws Exception {
        Mockito.when(especialidadService.findById(1L)).thenReturn(especialidad1);

        mockMvc.perform(get("/especialidades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Dermatología"));
    }

    @Test
    public void testCreateEspecialidad() throws Exception {
        Mockito.when(especialidadService.createEspecialidad(any(EspecialidadDTO.class))).thenReturn(especialidad1);

        mockMvc.perform(post("/especialidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(especialidad1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Dermatología"));
    }

    @Test
    public void testUpdateEspecialidad() throws Exception {
        especialidad1.setNombre("Oftalmología");

        Mockito.when(especialidadService.updateEspecialidad(eq(1L), any(EspecialidadDTO.class))).thenReturn(especialidad1);

        mockMvc.perform(put("/especialidades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(especialidad1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Oftalmología"));
    }

    @Test
    public void testDeleteEspecialidad() throws Exception {
        Mockito.doNothing().when(especialidadService).deleteEspecialidad(1L);

        mockMvc.perform(delete("/especialidades/1"))
                .andExpect(status().isOk());
    }
}
