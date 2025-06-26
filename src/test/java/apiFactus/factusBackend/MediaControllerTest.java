package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.UploadMediaDTO;
import apiFactus.factusBackend.Service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.nio.file.Files;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MediaControllerTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private MediaController mediaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mediaController).build();
    }

    @Test
    void uploadMedia_shouldReturnPath() throws Exception {
        // Simular archivo
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image-content".getBytes());

        // Simular respuesta del servicio
        when(storageService.store(any())).thenReturn("media/test.jpg");

        mockMvc.perform(multipart("/media/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.path").value("media/test.jpg"));

        verify(storageService, times(1)).store(any());
    }

    @Test
    void getResource_shouldReturnFile() throws Exception {
        // Simular archivo real
        byte[] content = "test content".getBytes();
        ByteArrayResource resource = new ByteArrayResource(content) {
            @Override
            public File getFile() {
                try {
                    File tempFile = File.createTempFile("test", ".txt");
                    Files.write(tempFile.toPath(), content);
                    tempFile.deleteOnExit();
                    return tempFile;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        when(storageService.loadAsResource("test.txt")).thenReturn(resource);

        mockMvc.perform(get("/media/test.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain"))
                .andExpect(content().bytes(content));
    }
}
