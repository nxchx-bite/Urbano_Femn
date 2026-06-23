package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdcutoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDTO productoResponseDTO;
    private ProductoRequestDTO productoRequestDTO;

    @BeforeEach
    void setUp() {
        productoResponseDTO = new ProductoResponseDTO(
                1L,
                "Polera Oversize Negra",
                "Polera oversize de algodón estilo urbano",
                new BigDecimal("12990"),
                20,
                "M",
                "Negro",
                true,
                "Poleras",
                "Urbano Femn"
        );

        productoRequestDTO = new ProductoRequestDTO(
                "Polera Oversize Negra",
                "Polera oversize de algodón estilo urbano",
                new BigDecimal("12990"),
                20,
                "M",
                "Negro",
                true,
                1L,
                1L
        );
    }

    @Test
    void obtenerTodos_debeRetornarListaDeProductos() throws Exception {
        when(productoService.obtenerTodos()).thenReturn(List.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Polera Oversize Negra"))
                .andExpect(jsonPath("$[0].categoriaNombre").value("Poleras"))
                .andExpect(jsonPath("$[0].marcaNombre").value("Urbano Femn"));

        verify(productoService, times(1)).obtenerTodos();
    }

    @Test
    void obtenerPorId_debeRetornarProductoCuandoExiste() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Polera Oversize Negra"))
                .andExpect(jsonPath("$.categoriaNombre").value("Poleras"))
                .andExpect(jsonPath("$.marcaNombre").value("Urbano Femn"));

        verify(productoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_debeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(productoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).obtenerPorId(99L);
    }

    @Test
    void crear_debeRetornarCreatedCuandoDatosSonValidos() throws Exception {
        when(productoService.guardar(any(ProductoRequestDTO.class))).thenReturn(productoResponseDTO);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Polera Oversize Negra"))
                .andExpect(jsonPath("$.categoriaNombre").value("Poleras"))
                .andExpect(jsonPath("$.marcaNombre").value("Urbano Femn"));

        verify(productoService, times(1)).guardar(any(ProductoRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarOkCuandoProductoExiste() throws Exception {
        when(productoService.actualizar(eq(1L), any(ProductoRequestDTO.class)))
                .thenReturn(Optional.of(productoResponseDTO));

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Polera Oversize Negra"));

        verify(productoService, times(1)).actualizar(eq(1L), any(ProductoRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarNotFoundCuandoProductoNoExiste() throws Exception {
        when(productoService.actualizar(eq(99L), any(ProductoRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequestDTO)))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).actualizar(eq(99L), any(ProductoRequestDTO.class));
    }

    @Test
    void eliminar_debeRetornarNoContentCuandoProductoExiste() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(productoResponseDTO));
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).obtenerPorId(1L);
        verify(productoService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_debeRetornarNotFoundCuandoProductoNoExiste() throws Exception {
        when(productoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());

        verify(productoService, times(1)).obtenerPorId(99L);
        verify(productoService, never()).eliminar(99L);
    }

    @Test
    void buscarPorNombre_debeRetornarProductosCoincidentes() throws Exception {
        when(productoService.buscarPorNombre("Polera")).thenReturn(List.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos/buscar")
                        .param("nombre", "Polera"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Polera Oversize Negra"));

        verify(productoService, times(1)).buscarPorNombre("Polera");
    }

    @Test
    void buscarPorCategoria_debeRetornarProductosDeUnaCategoria() throws Exception {
        when(productoService.buscarPorCategoria(1L)).thenReturn(List.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaNombre").value("Poleras"));

        verify(productoService, times(1)).buscarPorCategoria(1L);
    }

    @Test
    void buscarPorMarca_debeRetornarProductosDeUnaMarca() throws Exception {
        when(productoService.buscarPorMarca(1L)).thenReturn(List.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos/marca/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marcaNombre").value("Urbano Femn"));

        verify(productoService, times(1)).buscarPorMarca(1L);
    }

    @Test
    void buscarPorPrecioMaximo_debeRetornarProductosDentroDelPrecio() throws Exception {
        when(productoService.buscarPorPrecioMaximo(new BigDecimal("20000")))
                .thenReturn(List.of(productoResponseDTO));

        mockMvc.perform(get("/api/productos/precio")
                        .param("precioMax", "20000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Polera Oversize Negra"));

        verify(productoService, times(1)).buscarPorPrecioMaximo(new BigDecimal("20000"));
    }
}
