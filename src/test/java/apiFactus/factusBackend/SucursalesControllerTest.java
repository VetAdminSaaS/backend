package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.SucursalesDTO;
import apiFactus.factusBackend.Dto.SucursalesDetailsDTO;
import apiFactus.factusBackend.Repository.SucursalesRepository;
import apiFactus.factusBackend.Service.SucursalesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalesController.class)
class SucursalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SucursalesService sucursalesService;

    @MockBean
    private SucursalesRepository sucursalesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSucursales_debeRetornarLista() throws Exception {
        SucursalesDetailsDTO dto = new SucursalesDetailsDTO();
        dto.setNombre("Sucursal 1");

        Mockito.when(sucursalesService.getAllSucursales()).thenReturn(List.of(dto));

        mockMvc.perform(get("/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is("Sucursal 1")));
    }

    @Test
    void getSucursalPorId_debeRetornarSucursal() throws Exception {
        SucursalesDetailsDTO dto = new SucursalesDetailsDTO();
        dto.setNombre("Sucursal A");

        Mockito.when(sucursalesService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sucursal A")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearSucursal_debeRetornarSucursalCreada() throws Exception {
        SucursalesDTO dto = new SucursalesDTO();
        dto.setNombre("Nueva Sucursal");

        Mockito.when(sucursalesService.create(any(SucursalesDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/sucursales/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Nueva Sucursal")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSucursal_debeRetornarSucursalActualizada() throws Exception {
        SucursalesDTO dto = new SucursalesDTO();
        dto.setNombre("Sucursal Actualizada");

        Mockito.when(sucursalesService.update(anyLong(), any(SucursalesDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/sucursales/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Sucursal Actualizada")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarSucursal_debeRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/sucursales/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
