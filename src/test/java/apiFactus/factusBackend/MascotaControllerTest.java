package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.MascotaRequestDTO;
import apiFactus.factusBackend.Dto.MascotaResponseDTO;
import apiFactus.factusBackend.Service.MascotaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MascotaControllerTest {

    @Mock
    private MascotaService mascotaService;

    @InjectMocks
    private MascotaController mascotaController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mascotaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearMascota_shouldReturnCreated() throws Exception {
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombreCompleto("Luna");
        requestDTO.setDescripcion("Mascota feliz");
        requestDTO.setFechaNacimiento(LocalDate.of(2020, 1, 1));
        requestDTO.setEsterilizado(true);
        requestDTO.setPeso(4.5);
        requestDTO.setApoderadoIds(List.of(1L));

        MascotaResponseDTO responseDTO = new MascotaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombreCompleto("Luna");

        when(mascotaService.crearMascota(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombreCompleto").value("Luna"));
    }

    @Test
    void consultarMascota_shouldReturnOk() throws Exception {
        MascotaResponseDTO responseDTO = new MascotaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombreCompleto("Luna");

        when(mascotaService.obtenerMascotaPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void listarMascotas_shouldReturnList() throws Exception {
        MascotaResponseDTO m1 = new MascotaResponseDTO();
        m1.setId(1L);
        m1.setNombreCompleto("Luna");

        when(mascotaService.listarMascotas()).thenReturn(List.of(m1));

        mockMvc.perform(get("/mascotas/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Luna"));
    }

    @Test
    void listarMascotasByApoderado_shouldReturnList() throws Exception {
        MascotaResponseDTO m1 = new MascotaResponseDTO();
        m1.setId(2L);
        m1.setNombreCompleto("Max");

        when(mascotaService.listarMascotasByApoderado()).thenReturn(List.of(m1));

        mockMvc.perform(get("/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCompleto").value("Max"));
    }

    @Test
    void actualizarMascota_shouldReturnUpdated() throws Exception {
        MascotaRequestDTO requestDTO = new MascotaRequestDTO();
        requestDTO.setNombreCompleto("Rocky");

        MascotaResponseDTO responseDTO = new MascotaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombreCompleto("Rocky");

        when(mascotaService.actualizarMascota(eq(1L), any())).thenReturn(responseDTO);

        mockMvc.perform(put("/mascotas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Rocky"));
    }

    @Test
    void eliminarMascota_shouldReturnNoContent() throws Exception {
        doNothing().when(mascotaService).eliminarMascota(1L);

        mockMvc.perform(delete("/mascotas/1"))
                .andExpect(status().isNoContent());

        verify(mascotaService, times(1)).eliminarMascota(1L);
    }
}
