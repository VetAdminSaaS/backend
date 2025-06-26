package apiFactus.factusBackend;

import apiFactus.factusBackend.Controller.AdminiController;
import apiFactus.factusBackend.Dto.payment_methodRequestDTO;
import apiFactus.factusBackend.Dto.payment_methodResponseDTO;
import apiFactus.factusBackend.Service.paymentMethodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminiController.class)
public class AdminiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private paymentMethodService metodoPagoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearMetodoPago() throws Exception {
        // Arrange
        payment_methodRequestDTO request = new payment_methodRequestDTO();
        request.setNombre("Yape");
        request.setEstado(true);

        when(metodoPagoService.crearMetodoPago(any())).thenReturn(request);

        // Act & Assert
        mockMvc.perform(post("/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Yape"))
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    void testListarMetodoPago() throws Exception {
        // Arrange
        payment_methodResponseDTO dto1 = new payment_methodResponseDTO();
        dto1.setNombre("Yape");
        dto1.setEstado(true);

        payment_methodResponseDTO dto2 = new payment_methodResponseDTO();
        dto2.setNombre("Transferencia");
        dto2.setEstado(true);

        List<payment_methodResponseDTO> responseList = Arrays.asList(dto1, dto2);
        when(metodoPagoService.listarMetodoDePago()).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(get("/admin/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Yape"))
                .andExpect(jsonPath("$[1].nombre").value("Transferencia"));
    }
}
