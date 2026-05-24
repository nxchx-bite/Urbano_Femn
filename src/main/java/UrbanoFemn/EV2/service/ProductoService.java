package UrbanoFemn.EV2.service;

import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.model.Producto;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import UrbanoFemn.EV2.repository.MarcaRepository;
import UrbanoFemn.EV2.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

        private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    private ProductoResponseDTO mapToDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getTalla(),
                producto.getColor(),
                producto.getActivo(),
                producto.getCategoria().getNombre(),
                producto.getMarca().getNombre()
        );
    }

    public List<ProductoResponseDTO> obtenerTodos() {
        log.info("Listando todos los productos");

        return productoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductoResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);

        return productoRepository.findById(id)
                .map(this::mapToDTO);
    }

    public ProductoResponseDTO guardar(ProductoRequestDTO dto) {
        log.info("Creando producto: {}", dto.getNombre());

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException(
                        "Categoría no encontrada con id: " + dto.getCategoriaId()
                ));

        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException(
                        "Marca no encontrada con id: " + dto.getMarcaId()
                ));

        Producto producto = new Producto();

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setTalla(dto.getTalla());
        producto.setColor(dto.getColor());
        producto.setActivo(dto.getActivo());
        producto.setCategoria(categoria);
        producto.setMarca(marca);

        Producto guardado = productoRepository.save(producto);

        log.info("Producto creado correctamente con ID: {}", guardado.getId());

        return mapToDTO(guardado);
    }

    public Optional<ProductoResponseDTO> actualizar(Long id, ProductoRequestDTO dto) {
        log.info("Actualizando producto con ID: {}", id);

        return productoRepository.findById(id).map(existente -> {

            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException(
                            "Categoría no encontrada con id: " + dto.getCategoriaId()
                    ));

            Marca marca = marcaRepository.findById(dto.getMarcaId())
                    .orElseThrow(() -> new RuntimeException(
                            "Marca no encontrada con id: " + dto.getMarcaId()
                    ));

            existente.setNombre(dto.getNombre());
            existente.setDescripcion(dto.getDescripcion());
            existente.setPrecio(dto.getPrecio());
            existente.setStock(dto.getStock());
            existente.setTalla(dto.getTalla());
            existente.setColor(dto.getColor());
            existente.setActivo(dto.getActivo());
            existente.setCategoria(categoria);
            existente.setMarca(marca);

            Producto actualizado = productoRepository.save(existente);

            log.info("Producto actualizado correctamente con ID: {}", actualizado.getId());

            return mapToDTO(actualizado);
        });
    }

    public void eliminar(Long id) {
        log.warn("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);

        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> buscarPorCategoria(Long categoriaId) {
        log.info("Buscando productos por categoría ID: {}", categoriaId);

        return productoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> buscarPorMarca(Long marcaId) {
        log.info("Buscando productos por marca ID: {}", marcaId);

        return productoRepository.findByMarcaId(marcaId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoResponseDTO> buscarPorPrecioMaximo(BigDecimal precioMax) {
        log.info("Buscando productos con precio menor o igual a: {}", precioMax);

        return productoRepository.findByPrecioLessThanEqual(precioMax)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

}
