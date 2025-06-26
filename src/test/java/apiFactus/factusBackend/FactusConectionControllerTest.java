package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.integration.factus.halltec.Dto.*;
import apiFactus.factusBackend.integration.factus.halltec.Service.FacturaService;
import apiFactus.factusBackend.integration.factus.halltec.Service.FacturacionActionService;
import apiFactus.factusBackend.integration.factus.halltec.Service.FacturacionDataService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FactusConectionController.class)
@WithMockUser(roles = "ADMIN")
public class FactusConectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacturaService facturaService;

    @MockBean
    private FacturacionDataService facturacionDataService;

    @MockBean
    private FacturacionActionService facturacionActionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testObtenerRangoNumerico() throws Exception {
        Mockito.when(facturacionDataService.obtenerRangoNumerico())
                .thenReturn(List.of(new rangoNumericoDTO()));

        mockMvc.perform(get("/factus/rango-numerico"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerUnidadesMedida() throws Exception {
        Mockito.when(facturacionDataService.obtenerUnidades())
                .thenReturn(List.of(new unidadesDTO()));

        mockMvc.perform(get("/factus/unidades-medida"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerMunicipios() throws Exception {
        Mockito.when(facturacionDataService.obtenerMunicipios())
                .thenReturn(List.of(new municipioDTO()));

        mockMvc.perform(get("/factus/municipios"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerTributos() throws Exception {
        Mockito.when(facturacionDataService.obtenerTributos())
                .thenReturn(List.of(new tributoDTO()));

        mockMvc.perform(get("/factus/tributos"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCrearFactura() throws Exception {
        FacturaDTO facturaDTO = new FacturaDTO();
        Mockito.when(facturaService.CrearFactura(any(), eq(1)))
                .thenReturn(facturaDTO);

        mockMvc.perform(post("/factus/crearFactura/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCrearFacturaManual() throws Exception {
        FacturaDTO facturaDTO = new FacturaDTO();
        Mockito.when(facturaService.CrearFacturaManual(any()))
                .thenReturn(facturaDTO);

        mockMvc.perform(post("/factus/crearFacturaManual")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testVerFactura() throws Exception {
        Mockito.when(facturacionActionService.obtenerFactura("ABC123", "factura")).thenReturn("http://url-factura");
        Mockito.when(facturacionActionService.obtenerFactura("ABC123", "dian")).thenReturn("http://url-qr");

        mockMvc.perform(get("/factus/verFactura/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.bill.public_url").value("http://url-factura"))
                .andExpect(jsonPath("$.data.bill.qr").value("http://url-qr"));
    }

    @Test
    public void testDescargarFactura() throws Exception {
        Mockito.when(facturacionActionService.descargarFactura("ABC123")).thenReturn("PDF DATA".getBytes());

        mockMvc.perform(get("/factus/download-pdf/ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    public void testEliminarFactura() throws Exception {
        Mockito.when(facturacionActionService.eliminarFactura("REF123"))
                .thenReturn(Map.of("message", "Eliminado con éxito"));

        mockMvc.perform(delete("/factus/REF123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerFacturasFiltrado() throws Exception {
        Mockito.when(facturaService.obtenerFacturas(
                        any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Map.of("total", 1, "facturas", List.of(new FacturaDTO())));

        mockMvc.perform(get("/factus/obtener/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}

