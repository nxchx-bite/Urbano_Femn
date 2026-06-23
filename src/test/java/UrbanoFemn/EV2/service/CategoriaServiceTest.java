package UrbanoFemn.EV2.service;

import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaRequestDTO categoriaRequestDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(
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
    void obtenerTodas_debeRetornarListaDeCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> resultado = categoriaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Poleras", resultado.get(0).getNombre());
        assertEquals("Poleras femeninas urbanas", resultado.get(0).getDescripcion());
        assertTrue(resultado.get(0).getActiva());

        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarCategoriaCuandoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<CategoriaResponseDTO> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Poleras", resultado.get().getNombre());

        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_debeRetornarVacioCuandoNoExiste() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CategoriaResponseDTO> resultado = categoriaService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());

        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeCrearCategoriaCorrectamente() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO resultado = categoriaService.guardar(categoriaRequestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Poleras", resultado.getNombre());
        assertEquals("Poleras femeninas urbanas", resultado.getDescripcion());
        assertTrue(resultado.getActiva());

        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void actualizar_debeActualizarCategoriaCuandoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Optional<CategoriaResponseDTO> resultado = categoriaService.actualizar(1L, categoriaRequestDTO);

        assertTrue(resultado.isPresent());
        assertEquals("Poleras", resultado.get().getNombre());
        assertEquals("Poleras femeninas urbanas", resultado.get().getDescripcion());
        assertTrue(resultado.get().getActiva());

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void actualizar_debeRetornarVacioCuandoCategoriaNoExiste() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<CategoriaResponseDTO> resultado = categoriaService.actualizar(99L, categoriaRequestDTO);

        assertTrue(resultado.isEmpty());

        verify(categoriaRepository, times(1)).findById(99L);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void eliminar_debeEliminarCategoriaPorId() {
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.eliminar(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void buscarActivas_debeRetornarCategoriasActivas() {
        when(categoriaRepository.findByActivaTrue()).thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> resultado = categoriaService.buscarActivas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActiva());
        assertEquals("Poleras", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findByActivaTrue();
    }

    @Test
    void buscarPorNombre_debeRetornarCategoriasCoincidentes() {
        when(categoriaRepository.findByNombreContainingIgnoreCase("Poleras"))
                .thenReturn(List.of(categoria));

        List<CategoriaResponseDTO> resultado = categoriaService.buscarPorNombre("Poleras");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Poleras", resultado.get(0).getNombre());

        verify(categoriaRepository, times(1)).findByNombreContainingIgnoreCase("Poleras");
    }
}