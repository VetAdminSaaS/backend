package apiFactus.factusBackend;

import apiFactus.factusBackend.Dto.DireccionEnvioDTO;
import apiFactus.factusBackend.Service.DireccionEnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DireccionEnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DireccionEnvioService direccionEnvioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarDirecciones() throws Exception {
        DireccionEnvioDTO dto = new DireccionEnvioDTO();
        dto.setId(1L);
        dto.setDireccion("Av. Principal 123");
        dto.setCiudad("Trujillo");

        when(direccionEnvioService.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/direccion/envio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].direccion").value("Av. Principal 123"));
    }

    @Test
    void testObtenerDireccionPorId() throws Exception {
        DireccionEnvioDTO dto = new DireccionEnvioDTO();
        dto.setId(2L);
        dto.setCiudad("Lima");

        when(direccionEnvioService.findById(2L)).thenReturn(dto);

        mockMvc.perform(get("/direccion/envio/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.ciudad").value("Lima"));
    }

    @Test
    void testCrearDireccion() throws Exception {
        DireccionEnvioDTO dtoRequest = new DireccionEnvioDTO();
        dtoRequest.setDireccion("Mz M Lote 8");
        dtoRequest.setCiudad("Arequipa");

        DireccionEnvioDTO dtoResponse = new DireccionEnvioDTO();
        dtoResponse.setId(5L);
        dtoResponse.setDireccion("Mz M Lote 8");
        dtoResponse.setCiudad("Arequipa");

        when(direccionEnvioService.create(any())).thenReturn(dtoResponse);

        mockMvc.perform(post("/direccion/envio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.ciudad").value("Arequipa"));
    }

    @Test
    void testActualizarDireccion() throws Exception {
        DireccionEnvioDTO dtoUpdate = new DireccionEnvioDTO();
        dtoUpdate.setDireccion("Av. Los Héroes");
        dtoUpdate.setCiudad("Chiclayo");

        DireccionEnvioDTO dtoUpdated = new DireccionEnvioDTO();
        dtoUpdated.setId(10L);
        dtoUpdated.setDireccion("Av. Los Héroes");
        dtoUpdated.setCiudad("Chiclayo");

        when(direccionEnvioService.update(10L, dtoUpdate)).thenReturn(dtoUpdated);

        mockMvc.perform(put("/direccion/envio/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.ciudad").value("Chiclayo"));
    }

    @Test
    void testEliminarDireccion() throws Exception {
        doNothing().when(direccionEnvioService).delete(7L);

        mockMvc.perform(delete("/direccion/envio/eliminar/7"))
                .andExpect(status().isNoContent());
    }
}
