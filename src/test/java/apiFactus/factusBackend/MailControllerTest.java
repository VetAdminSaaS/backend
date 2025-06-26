package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Service.RecuperarContrasenaTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RecuperarContrasenaTokenService recuperarContrasenaTokenService;

    @InjectMocks
    private MailController mailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mailController).build();
    }

    @Test
    void enviarRecuperarContrasenaEmail_shouldReturnOk() throws Exception {
        String jsonBody = "{\"email\":\"test@example.com\"}";

        doNothing().when(recuperarContrasenaTokenService).createAndSendPasswordResetToken("test@example.com");

        mockMvc.perform(post("/mail/sendMail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        verify(recuperarContrasenaTokenService, times(1)).createAndSendPasswordResetToken("test@example.com");
    }

    @Test
    void verificarTokenValido_shouldReturnTrue() throws Exception {
        when(recuperarContrasenaTokenService.isValidToken("abc123")).thenReturn(true);

        mockMvc.perform(get("/mail/reset/check/abc123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(recuperarContrasenaTokenService, times(1)).isValidToken("abc123");
    }

    @Test
    void recuperarContrasena_shouldReturnOk() throws Exception {
        String jsonBody = "{\"newContrasena\":\"newStrongPassword123\"}";

        doNothing().when(recuperarContrasenaTokenService).recuperarContrasena("abc123", "newStrongPassword123");

        mockMvc.perform(post("/mail/reset/abc123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        verify(recuperarContrasenaTokenService, times(1)).recuperarContrasena("abc123", "newStrongPassword123");
    }
}
