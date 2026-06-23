package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.service.MarcaService;
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

@WebMvcTest(MarcaController.class)
class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarcaService marcaService;

    @Autowired
    private ObjectMapper objectMapper;

    private MarcaResponseDTO marcaResponseDTO;
    private MarcaRequestDTO marcaRequestDTO;

    @BeforeEach
    void setUp() {
        marcaResponseDTO = new MarcaResponseDTO(
                1L,
                "Urbano Femn",
                "Marca propia de la tienda",
                "Chile",
                true
        );

        marcaRequestDTO = new MarcaRequestDTO(
                "Urbano Femn",
                "Marca propia de la tienda",
                "Chile",
                true
        );
    }

    @Test
    void obtenerTodas_debeRetornarListaDeMarcas() throws Exception {
        when(marcaService.obtenerTodas()).thenReturn(List.of(marcaResponseDTO));

        mockMvc.perform(get("/api/marcas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$[0].descripcion").value("Marca propia de la tienda"))
                .andExpect(jsonPath("$[0].paisOrigen").value("Chile"))
                .andExpect(jsonPath("$[0].activa").value(true));

        verify(marcaService, times(1)).obtenerTodas();
    }

    @Test
    void obtenerPorId_debeRetornarMarcaCuandoExiste() throws Exception {
        when(marcaService.obtenerPorId(1L)).thenReturn(Optional.of(marcaResponseDTO));

        mockMvc.perform(get("/api/marcas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$.paisOrigen").value("Chile"))
                .andExpect(jsonPath("$.activa").value(true));

        verify(marcaService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_debeRetornarNotFoundCuandoNoExiste() throws Exception {
        when(marcaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/marcas/99"))
                .andExpect(status().isNotFound());

        verify(marcaService, times(1)).obtenerPorId(99L);
    }

    @Test
    void crear_debeRetornarCreatedCuandoDatosSonValidos() throws Exception {
        when(marcaService.guardar(any(MarcaRequestDTO.class))).thenReturn(marcaResponseDTO);

        mockMvc.perform(post("/api/marcas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marcaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$.descripcion").value("Marca propia de la tienda"))
                .andExpect(jsonPath("$.paisOrigen").value("Chile"))
                .andExpect(jsonPath("$.activa").value(true));

        verify(marcaService, times(1)).guardar(any(MarcaRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarOkCuandoMarcaExiste() throws Exception {
        when(marcaService.actualizar(eq(1L), any(MarcaRequestDTO.class)))
                .thenReturn(Optional.of(marcaResponseDTO));

        mockMvc.perform(put("/api/marcas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marcaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$.paisOrigen").value("Chile"));

        verify(marcaService, times(1)).actualizar(eq(1L), any(MarcaRequestDTO.class));
    }

    @Test
    void actualizar_debeRetornarNotFoundCuandoMarcaNoExiste() throws Exception {
        when(marcaService.actualizar(eq(99L), any(MarcaRequestDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/marcas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(marcaRequestDTO)))
                .andExpect(status().isNotFound());

        verify(marcaService, times(1)).actualizar(eq(99L), any(MarcaRequestDTO.class));
    }

    @Test
    void eliminar_debeRetornarNoContentCuandoMarcaExiste() throws Exception {
        when(marcaService.obtenerPorId(1L)).thenReturn(Optional.of(marcaResponseDTO));
        doNothing().when(marcaService).eliminar(1L);

        mockMvc.perform(delete("/api/marcas/1"))
                .andExpect(status().isNoContent());

        verify(marcaService, times(1)).obtenerPorId(1L);
        verify(marcaService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_debeRetornarNotFoundCuandoMarcaNoExiste() throws Exception {
        when(marcaService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/marcas/99"))
                .andExpect(status().isNotFound());

        verify(marcaService, times(1)).obtenerPorId(99L);
        verify(marcaService, never()).eliminar(99L);
    }

    @Test
    void buscarActivas_debeRetornarMarcasActivas() throws Exception {
        when(marcaService.buscarActivas()).thenReturn(List.of(marcaResponseDTO));

        mockMvc.perform(get("/api/marcas/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$[0].activa").value(true));

        verify(marcaService, times(1)).buscarActivas();
    }

    @Test
    void buscarPorNombre_debeRetornarMarcasCoincidentes() throws Exception {
        when(marcaService.buscarPorNombre("Urbano")).thenReturn(List.of(marcaResponseDTO));

        mockMvc.perform(get("/api/marcas/buscar")
                        .param("nombre", "Urbano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Urbano Femn"))
                .andExpect(jsonPath("$[0].paisOrigen").value("Chile"));

        verify(marcaService, times(1)).buscarPorNombre("Urbano");
    }
}
