package apiFactus.factusBackend;

import apiFactus.factusBackend.Controller.AdminProductoController;
import apiFactus.factusBackend.Dto.*;
import apiFactus.factusBackend.Domain.Entity.productos_Tienda;
import apiFactus.factusBackend.Service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminProductoController.class)
public class AdminProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListarProductos() throws Exception {
        ProductoDetailsDTO producto = new ProductoDetailsDTO();
        producto.setNombre("Croquetas");
        producto.setPrecio(25.0);

        when(productoService.getAllProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/productos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Croquetas"));
    }

    @Test
    void testGetProductoById() throws Exception {
        ProductoDetailsDTO producto = new ProductoDetailsDTO();
        producto.setNombre("Juguete");

        when(productoService.findById(1)).thenReturn(producto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juguete"));
    }

    @Test
    void testGetTotalProductos() throws Exception {
        when(productoService.getTotalProducts()).thenReturn(15L);

        mockMvc.perform(get("/productos/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));
    }

    @Test
    void testPaginacionProductos() throws Exception {
        ProductoDetailsDTO producto = new ProductoDetailsDTO();
        producto.setNombre("Arena sanitaria");

        Page<ProductoDetailsDTO> page = new PageImpl<>(List.of(producto), PageRequest.of(0, 20), 1);

        when(productoService.paginate(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/productos/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Arena sanitaria"));
    }

    @Test
    void testBuscarProductoPorNombre() throws Exception {
        productos_Tienda producto = new productos_Tienda();
        producto.setNombre("Antipulgas");

        when(productoService.obtenerProductoPorNombre("Antipulgas"))
                .thenReturn(List.of(producto));

        mockMvc.perform(get("/productos/nombre/Antipulgas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Antipulgas"));
    }

    @Test
    void testBuscarProductoPorNombreSinNombre() throws Exception {
        mockMvc.perform(get("/productos/nombre/ "))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[]"));
    }

    @Test
    void testEliminarProducto() throws Exception {
        mockMvc.perform(delete("/productos/eliminar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerPorCategoria() throws Exception {
        ProductoDetailsDTO producto = new ProductoDetailsDTO();
        producto.setNombre("Galletas");

        when(productoService.obtenerProductosPorCategory(3)).thenReturn(List.of(producto));

        mockMvc.perform(get("/productos/category/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Galletas"));
    }

    @Test
    void testTiposEntrega() throws Exception {
        TipoEntregaResponse response = new TipoEntregaResponse();
        response.setTipoEntrega(List.of("Domicilio", "Recojo en tienda"));

        when(productoService.getTiposEntregaPorProducto(1L)).thenReturn(response);

        mockMvc.perform(get("/productos/1/tipos-entrega"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoEntrega[0]").value("Domicilio"));
    }

    @Test
    void testSucursalesConStock() throws Exception {
        SucursalProductoResponseDTO response = new SucursalProductoResponseDTO();
        response.setSucursales(List.of("Sucursal Norte"));

        when(productoService.getSucursalesPorProducto(1L)).thenReturn(response);

        mockMvc.perform(get("/productos/1/sucursales-con-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursales[0]").value("Sucursal Norte"));
    }

    @Test
    void testSucursalesConStockSinResultados() throws Exception {
        SucursalProductoResponseDTO response = new SucursalProductoResponseDTO();
        response.setSucursales(Collections.emptyList());

        when(productoService.getSucursalesPorProducto(2L)).thenReturn(response);

        mockMvc.perform(get("/productos/2/sucursales-con-stock"))
                .andExpect(status().isNoContent());
    }
}
