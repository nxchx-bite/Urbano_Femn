package UrbanoFemn.EV2.service;
import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.model.Producto;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import UrbanoFemn.EV2.repository.MarcaRepository;
import UrbanoFemn.EV2.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Marca marca;
    private Producto producto;
    private ProductoRequestDTO productoRequestDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(
                1L,
                "Poleras",
                "Poleras femeninas urbanas",
                true
        );

        marca = new Marca(
                1L,
                "Urbano Femn",
                "Marca propia de la tienda",
                "Chile",
                true
        );

        producto = new Producto(
                1L,
                "Polera Oversize Negra",
                "Polera oversize de algodón estilo urbano",
                new BigDecimal("12990"),
                20,
                "M",
                "Negro",
                true,
                categoria,
                marca
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
    void obtenerTodos_debeRetornarListaDeProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Polera Oversize Negra", resultado.get(0).getNombre());
        assertEquals("Poleras", resultado.get(0).getCategoriaNombre());
        assertEquals("Urbano Femn", resultado.get(0).getMarcaNombre());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_debeRetornarProductoCuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<ProductoResponseDTO> resultado = productoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Polera Oversize Negra", resultado.get().getNombre());

        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_debeRetornarVacioCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductoResponseDTO> resultado = productoService.obtenerPorId(99L);

        assertTrue(resultado.isEmpty());

        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    void guardar_debeCrearProductoCuandoCategoriaYMarcaExisten() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO resultado = productoService.guardar(productoRequestDTO);

        assertNotNull(resultado);
        assertEquals("Polera Oversize Negra", resultado.getNombre());
        assertEquals(new BigDecimal("12990"), resultado.getPrecio());
        assertEquals("Poleras", resultado.getCategoriaNombre());
        assertEquals("Urbano Femn", resultado.getMarcaNombre());

        verify(categoriaRepository, times(1)).findById(1L);
        verify(marcaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void guardar_debeLanzarExcepcionCuandoCategoriaNoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productoService.guardar(productoRequestDTO)
        );

        assertEquals("Categoría no encontrada con id: 1", exception.getMessage());

        verify(categoriaRepository, times(1)).findById(1L);
        verify(marcaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void guardar_debeLanzarExcepcionCuandoMarcaNoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productoService.guardar(productoRequestDTO)
        );

        assertEquals("Marca no encontrada con id: 1", exception.getMessage());

        verify(categoriaRepository, times(1)).findById(1L);
        verify(marcaRepository, times(1)).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void actualizar_debeActualizarProductoCuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(marcaRepository.findById(1L)).thenReturn(Optional.of(marca));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Optional<ProductoResponseDTO> resultado = productoService.actualizar(1L, productoRequestDTO);

        assertTrue(resultado.isPresent());
        assertEquals("Polera Oversize Negra", resultado.get().getNombre());
        assertEquals("Poleras", resultado.get().getCategoriaNombre());
        assertEquals("Urbano Femn", resultado.get().getMarcaNombre());

        verify(productoRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).findById(1L);
        verify(marcaRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizar_debeRetornarVacioCuandoProductoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductoResponseDTO> resultado = productoService.actualizar(99L, productoRequestDTO);

        assertTrue(resultado.isEmpty());

        verify(productoRepository, times(1)).findById(99L);
        verify(categoriaRepository, never()).findById(anyLong());
        verify(marcaRepository, never()).findById(anyLong());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void buscarPorNombre_debeRetornarProductosCoincidentes() {
        when(productoRepository.findByNombreContainingIgnoreCase("Polera"))
                .thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.buscarPorNombre("Polera");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Polera Oversize Negra", resultado.get(0).getNombre());

        verify(productoRepository, times(1)).findByNombreContainingIgnoreCase("Polera");
    }

    @Test
    void buscarPorCategoria_debeRetornarProductosDeUnaCategoria() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.buscarPorCategoria(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Poleras", resultado.get(0).getCategoriaNombre());

        verify(productoRepository, times(1)).findByCategoriaId(1L);
    }

    @Test
    void buscarPorMarca_debeRetornarProductosDeUnaMarca() {
        when(productoRepository.findByMarcaId(1L)).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.buscarPorMarca(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Urbano Femn", resultado.get(0).getMarcaNombre());

        verify(productoRepository, times(1)).findByMarcaId(1L);
    }

    @Test
    void buscarPorPrecioMaximo_debeRetornarProductosConPrecioMenorOIgual() {
        BigDecimal precioMax = new BigDecimal("20000");

        when(productoRepository.findByPrecioLessThanEqual(precioMax))
                .thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = productoService.buscarPorPrecioMaximo(precioMax);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getPrecio().compareTo(precioMax) <= 0);

        verify(productoRepository, times(1)).findByPrecioLessThanEqual(precioMax);
    }

    @Test
    void eliminar_debeEliminarProductoPorId() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminar(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
