package apiFactus.factusBackend;

import apiFactus.factusBackend.Dto.PaymentCaptureResponse;
import apiFactus.factusBackend.Dto.PaymentOrderResponse;
import apiFactus.factusBackend.Dto.PaymentStatusResponse;
import apiFactus.factusBackend.Service.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckoutService checkoutService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePaymentOrder() throws Exception {
        // Arrange
        PaymentOrderResponse response = new PaymentOrderResponse();
        response.setId("ORDER123");
        response.setStatus("CREATED");

        when(checkoutService.createPayment(any(), any(), any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/checkout/create")
                        .param("purchaseId", "1")
                        .param("returnUrl", "http://return.com")
                        .param("cancelUrl", "http://cancel.com")
                        .param("paymentProvider", "paypal"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ORDER123"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void testCapturePaymentOrder_completed() throws Exception {
        // Arrange
        PaymentCaptureResponse response = new PaymentCaptureResponse();
        response.setCompleted(true);

        when(checkoutService.capturePayment(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/checkout/capture")
                        .param("orderId", "ORDER123"))
                .andExpect(status().isOk());
    }

    @Test
    void testCapturePaymentOrder_notCompleted() throws Exception {
        // Arrange
        PaymentCaptureResponse response = new PaymentCaptureResponse();
        response.setCompleted(false);

        when(checkoutService.capturePayment(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/checkout/capture")
                        .param("orderId", "ORDER123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPaymentStatus() throws Exception {
        // Arrange
        PaymentStatusResponse response = new PaymentStatusResponse();
        response.setEstado("COMPLETED");

        when(checkoutService.verificarEstadoCompra(any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/checkout/status/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETED"));
    }
}
