package UrbanoFemn.EV2.service;
import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.repository.MarcaRepository;
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
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;

    private Marca marca;
    private MarcaRequestDTO marcaRequestDTO;

    @BeforeEach
    void setUp() {
        marca = new Marca(
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
    void obtenerTodas_debeRetornarListaDeMarcas() {
        when(marcaRepository.findAll()).thenReturn(List.of(marca));

        List<MarcaResponseDTO> resultado = marcaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Urbano Femn", resultado.get(0).getNombre());
        assertEquals("Marca propia de la tienda", resultado.get(0).getDescripcion());
        assertEquals("Chile", resultado.get(0).getPaisOrigen());
        assertTrue(resultado.get(0).getActiva());

        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarMarcaCuandoExiste() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));

        Optional<MarcaResponseDTO> resultado = marcaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Urbano Femn", resultado.get().getNombre());
        assertEquals("Chile", resultado.get().getPaisOrigen());

        verify(marcaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_debeRetornarVacioCuandoNoExiste() {
        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MarcaResponseDTO> resultado = marcaService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());

        verify(marcaRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeCrearMarcaCorrectamente() {
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);

        MarcaResponseDTO resultado = marcaService.guardar(marcaRequestDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Urbano Femn", resultado.getNombre());
        assertEquals("Marca propia de la tienda", resultado.getDescripcion());
        assertEquals("Chile", resultado.getPaisOrigen());
        assertTrue(resultado.getActiva());

        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    void actualizar_debeActualizarMarcaCuandoExiste() {
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(marcaRepository.save(any(Marca.class))).thenReturn(marca);

        Optional<MarcaResponseDTO> resultado = marcaService.actualizar(1L, marcaRequestDTO);

        assertTrue(resultado.isPresent());
        assertEquals("Urbano Femn", resultado.get().getNombre());
        assertEquals("Marca propia de la tienda", resultado.get().getDescripcion());
        assertEquals("Chile", resultado.get().getPaisOrigen());
        assertTrue(resultado.get().getActiva());

        verify(marcaRepository, times(1)).findById(1L);
        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    void actualizar_debeRetornarVacioCuandoMarcaNoExiste() {
        when(marcaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<MarcaResponseDTO> resultado = marcaService.actualizar(99L, marcaRequestDTO);

        assertTrue(resultado.isEmpty());

        verify(marcaRepository, times(1)).findById(99L);
        verify(marcaRepository, never()).save(any(Marca.class));
    }

    @Test
    void eliminar_debeEliminarMarcaPorId() {
        doNothing().when(marcaRepository).deleteById(1L);

        marcaService.eliminar(1L);

        verify(marcaRepository, times(1)).deleteById(1L);
    }

    @Test
    void buscarActivas_debeRetornarMarcasActivas() {
        when(marcaRepository.findByActivaTrue()).thenReturn(List.of(marca));

        List<MarcaResponseDTO> resultado = marcaService.buscarActivas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActiva());
        assertEquals("Urbano Femn", resultado.get(0).getNombre());

        verify(marcaRepository, times(1)).findByActivaTrue();
    }

    @Test
    void buscarPorNombre_debeRetornarMarcasCoincidentes() {
        when(marcaRepository.findByNombreContainingIgnoreCase("Urbano"))
                .thenReturn(List.of(marca));

        List<MarcaResponseDTO> resultado = marcaService.buscarPorNombre("Urbano");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Urbano Femn", resultado.get(0).getNombre());

        verify(marcaRepository, times(1)).findByNombreContainingIgnoreCase("Urbano");
    }
}