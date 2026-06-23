package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoriaResponseDTO categoriaResponseDTO;
    private CategoriaRequestDTO categoriaRequestDTO;

    @BeforeEach
    void setUp() {
        categoriaResponseDTO = new CategoriaResponseDTO(
                1L,
                "Poleras",
                "Poleras femeninas urbanas",
                true
        );

        categoriaRequestDTO = new CategoriaRequestDTO(
                "Poleras",
                "Poleras femeninas urbanas",
                true
        );
    }

    @Test
    void obtenerTodas_debeRetornarListaDeCategorias() throws Exception {
        when(categoriaService.obtenerTodas()).thenReturn(List.of(categoriaResponseDTO));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Poleras"))
                .andExpect(jsonPath("$[0].descripcion").value("Poleras femeninas urbanas"))
                .andExpect(jsonPath("$[0].activa").value(true));

        verify(categoriaService, times(1)).obtenerTodas();
    }

    @Test
    void obtenerPorId_debeRetornarCategoriaCuandoExiste() throws Exception {
        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoriaResponseDTO));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Poleras"))
                .andExpect(jsonPath("$.activa").value(true));

        verify(categoriaService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_debeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).obtenerPorId(99L);
    }

    @Test
    void crear_debeRetornarCreatedCuandoDatosSonValidos() throws Exception {
        when(categoriaService.guardar(any(CategoriaRequestDTO.class))).thenReturn(categoriaResponseDTO);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Poleras"))
                .andExpect(jsonPath("$.descripcion").value("Poleras femeninas urbanas"))
                .andExpect(jsonPath("$.activa").value(true));

        verify(categoriaService, times(1)).guardar(any(CategoriaRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarOkCuandoCategoriaExiste() throws Exception {
        when(categoriaService.actualizar(eq(1L), any(CategoriaRequestDTO.class)))
                .thenReturn(Optional.of(categoriaResponseDTO));

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Poleras"));

        verify(categoriaService, times(1)).actualizar(eq(1L), any(CategoriaRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarNotFoundCuandoCategoriaNoExiste() throws Exception {
        when(categoriaService.actualizar(eq(99L), any(CategoriaRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/categorias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRequestDTO)))
                .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).actualizar(eq(99L), any(CategoriaRequestDTO.class));
    }

    @Test
    void eliminar_debeRetornarNoContentCuandoCategoriaExiste() throws Exception {
        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoriaResponseDTO));
        doNothing().when(categoriaService).eliminar(1L);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).obtenerPorId(1L);
        verify(categoriaService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_debeRetornarNotFoundCuandoCategoriaNoExiste() throws Exception {
        when(categoriaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/categorias/99"))
                .andExpect(status().isNotFound());

        verify(categoriaService, times(1)).obtenerPorId(99L);
        verify(categoriaService, never()).eliminar(99L);
    }

    @Test
    void buscarActivas_debeRetornarCategoriasActivas() throws Exception {
        when(categoriaService.buscarActivas()).thenReturn(List.of(categoriaResponseDTO));

        mockMvc.perform(get("/api/categorias/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Poleras"))
                .andExpect(jsonPath("$[0].activa").value(true));

        verify(categoriaService, times(1)).buscarActivas();
    }

    @Test
    void buscarPorNombre_debeRetornarCategoriasCoincidentes() throws Exception {
        when(categoriaService.buscarPorNombre("Poleras")).thenReturn(List.of(categoriaResponseDTO));

        mockMvc.perform(get("/api/categorias/buscar")
                        .param("nombre", "Poleras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Poleras"));

        verify(categoriaService, times(1)).buscarPorNombre("Poleras");
    }
}
