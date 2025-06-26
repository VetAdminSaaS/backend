package apiFactus.factusBackend;

import apiFactus.factusBackend.Dto.PurchaseDTO;
import apiFactus.factusBackend.Service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetLastSixPaidPurchaseByAuthenticationUser() throws Exception {
        // Mock de una compra
        PurchaseDTO dto = new PurchaseDTO();
        dto.setId(1);
        dto.setTotalAmount(150.75);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setPaid(true);

        when(storeService.getLastSixPaidPurchasesByAuthenticatedUser()).thenReturn(List.of(dto));

        mockMvc.perform(get("/customer/last-six")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].totalAmount").value(150.75))
                .andExpect(jsonPath("$[0].paid").value(true));
    }
}
