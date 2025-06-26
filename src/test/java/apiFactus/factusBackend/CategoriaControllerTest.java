package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.CategoriaDTO;
import apiFactus.factusBackend.Service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCategorias() throws Exception {
        CategoriaDTO categoria1 = new CategoriaDTO();
        categoria1.setId(1);
        categoria1.setNombre("Alimentos");
        categoria1.setDescripcion("Comida para mascotas");

        CategoriaDTO categoria2 = new CategoriaDTO();
        categoria2.setId(2);
        categoria2.setNombre("Juguetes");
        categoria2.setDescripcion("Juguetes para perros y gatos");

        List<CategoriaDTO> lista = Arrays.asList(categoria1, categoria2);

        when(categoriaService.getAll()).thenReturn(lista);

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Alimentos"));
    }

    @Test
    void testGetCategoriaById() throws Exception {
        CategoriaDTO categoria = new CategoriaDTO();
        categoria.setId(1);
        categoria.setNombre("Salud");
        categoria.setDescripcion("Productos médicos");

        when(categoriaService.findById(1)).thenReturn(categoria);

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Salud"))
                .andExpect(jsonPath("$.descripcion").value("Productos médicos"));
    }

    @Test
    void testCreateCategoria() throws Exception {
        CategoriaDTO request = new CategoriaDTO();
        request.setNombre("Accesorios");
        request.setDescripcion("Correas, collares y más");

        CategoriaDTO response = new CategoriaDTO();
        response.setId(10);
        response.setNombre(request.getNombre());
        response.setDescripcion(request.getDescripcion());

        when(categoriaService.create(any())).thenReturn(response);

        mockMvc.perform(post("/categorias/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Accesorios"));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        CategoriaDTO request = new CategoriaDTO();
        request.setNombre("Actualizado");
        request.setDescripcion("Descripción actualizada");

        CategoriaDTO updated = new CategoriaDTO();
        updated.setId(1);
        updated.setNombre("Actualizado");
        updated.setDescripcion("Descripción actualizada");

        when(categoriaService.update(eq(1), any())).thenReturn(updated);

        mockMvc.perform(put("/categorias/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"))
                .andExpect(jsonPath("$.descripcion").value("Descripción actualizada"));
    }

    @Test
    void testDeleteCategoria() throws Exception {
        doNothing().when(categoriaService).delete(1);

        mockMvc.perform(delete("/categorias/eliminar/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).delete(1);
    }
}
