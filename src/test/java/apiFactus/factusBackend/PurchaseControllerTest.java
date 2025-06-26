package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.PurchaseCreateUpadteDTO;
import apiFactus.factusBackend.Dto.PurchaseDTO;
import apiFactus.factusBackend.Service.PurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(purchaseController).build();
    }

    @Test
    void crearCompra_shouldReturnCreatedPurchase() throws Exception {
        PurchaseCreateUpadteDTO requestDTO = new PurchaseCreateUpadteDTO();
        PurchaseDTO responseDTO = new PurchaseDTO();

        when(purchaseService.createPurchase(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void listarTodasCompras_shouldReturnList() throws Exception {
        when(purchaseService.getAllPurchases()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/purchases")
                        .with(request -> {
                            request.addHeader("Authorization", "Bearer test-token");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void confirmPurchase_shouldReturnConfirmed() throws Exception {
        PurchaseDTO dto = new PurchaseDTO();
        when(purchaseService.confirmPurchase(1)).thenReturn(dto);

        mockMvc.perform(put("/purchases/confirm/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPurchaseById_shouldReturnPurchase() throws Exception {
        PurchaseDTO dto = new PurchaseDTO();
        when(purchaseService.getPurchaseById(1)).thenReturn(dto);

        mockMvc.perform(get("/purchases/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getPurchaseStatus_shouldReturnNotFoundWhenNull() throws Exception {
        when(purchaseService.getPurchaseById(1)).thenReturn(null);

        mockMvc.perform(get("/purchases/1/status"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTotalPaidPurchases_shouldReturnTotal() throws Exception {
        when(purchaseService.getTotalPaidPurchases()).thenReturn(150.5);

        mockMvc.perform(get("/purchases/total-paid"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.5"));
    }

    @Test
    void getPurchaseByUser_shouldReturnList() throws Exception {
        when(purchaseService.getPurchaseHistoryByUserId()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/purchases/user"))
                .andExpect(status().isOk());
    }

    @Test
    void contarCompras_shouldReturnCount() throws Exception {
        when(purchaseService.contarComprasConNumero()).thenReturn(5L);

        mockMvc.perform(get("/purchases/facturas"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void obtenerVentasDelMes_shouldReturnMap() throws Exception {
        Map<String, Double> ventas = new HashMap<>();
        ventas.put("actual", 1000.0);
        ventas.put("anterior", 900.0);

        when(purchaseService.obtenerVentasMesActualYAnterior()).thenReturn(ventas);

        mockMvc.perform(get("/purchases/sales/this-month"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerComparacionVentas_shouldReturnMap() throws Exception {
        Map<String, Object> comparacion = new HashMap<>();
        comparacion.put("crecimiento", 10);

        when(purchaseService.obtenerComparacionVentas()).thenReturn(comparacion);

        mockMvc.perform(get("/purchases/sales-comparison"))
                .andExpect(status().isOk());
    }
}
