package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.UserProfileDTO;
import apiFactus.factusBackend.Dto.UsuariosStoreDTO;
import apiFactus.factusBackend.Service.StoreService;
import apiFactus.factusBackend.Service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void obtenerMiPerfil_debeRetornarPerfilDelUsuarioAutenticado() throws Exception {
        UsuariosStoreDTO dto = new UsuariosStoreDTO();
        dto.setNombre("Juan");
        Mockito.when(storeService.obtenerUsuarioAutenticado()).thenReturn(dto);

        mockMvc.perform(get("/usuarios/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void list_debeRetornarTodosLosUsuarios() throws Exception {
        UserProfileDTO user1 = new UserProfileDTO();
        user1.setNombre("Alice");

        UserProfileDTO user2 = new UserProfileDTO();
        user2.setNombre("Bob");

        Mockito.when(storeService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Alice")))
                .andExpect(jsonPath("$[1].nombre", is("Bob")));
    }

    @Test
    void totalUsuarios_debeRetornarElTotal() throws Exception {
        Mockito.when(usuarioService.getTotalUsuarios()).thenReturn(5L);

        mockMvc.perform(get("/usuarios/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void obtenerUsuarioPorId_debeRetornarUsuario() throws Exception {
        UserProfileDTO user = new UserProfileDTO();
        user.setNombre("Carlos");

        Mockito.when(usuarioService.getUserProfileById(1)).thenReturn(user);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos")));
    }

    @Test
    void updateProfileUsuario_debeActualizarYRetornarUsuario() throws Exception {
        UserProfileDTO update = new UserProfileDTO();
        update.setNombre("Ana");

        Mockito.when(usuarioService.updateUserProfile(anyInt(), any(UserProfileDTO.class))).thenReturn(update);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Ana")));
    }
}
