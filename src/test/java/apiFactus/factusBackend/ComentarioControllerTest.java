package apiFactus.factusBackend;

import apiFactus.factusBackend.Dto.ComentarioRequestDTO;
import apiFactus.factusBackend.Service.ComentarioProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioProductoService comentarioProductoService;

    @Autowired
    private ObjectMapper objectMapper;

    ComentarioRequestDTO sampleComentario() {
        ComentarioRequestDTO dto = new ComentarioRequestDTO();
        dto.setComentario("Muy buen producto");
        dto.setRating(5);
        dto.setUsuarioId(1);
        dto.setNombreUsuario("Juan");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void testGetAllComentariosPorProducto() throws Exception {
        ComentarioRequestDTO dto = sampleComentario();

        when(comentarioProductoService.getAllComentariosPorProducto(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/comentario/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].comentario").value("Muy buen producto"))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void testPaginateComentario() throws Exception {
        ComentarioRequestDTO dto = sampleComentario();
        Page<ComentarioRequestDTO> page = new PageImpl<>(List.of(dto));

        when(comentarioProductoService.getComentariosPorProducto(any(), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/comentario/page")
                        .param("productoId", "1")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].comentario").value("Muy buen producto"))
                .andExpect(jsonPath("$.content[0].rating").value(5));
    }

    @Test
    void testGetComentarioById() throws Exception {
        ComentarioRequestDTO dto = sampleComentario();

        when(comentarioProductoService.finById(1)).thenReturn(dto);

        mockMvc.perform(get("/comentario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Muy buen producto"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testCreateComentario() throws Exception {
        ComentarioRequestDTO dto = sampleComentario();

        when(comentarioProductoService.create(any(), any())).thenReturn(dto);

        mockMvc.perform(post("/comentario/producto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Muy buen producto"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testDeleteComentario() throws Exception {
        mockMvc.perform(delete("/comentario/eliminar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerPromedioRating() throws Exception {
        when(comentarioProductoService.obtenePromedioRatingProducto(1)).thenReturn(4.5);

        mockMvc.perform(get("/comentario/producto/promedio/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }
}
