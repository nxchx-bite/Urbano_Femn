package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProdcutoController {

    private final ProductoService productoService;

  
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody ProductoRequestDTO dto) {

        ProductoResponseDTO nuevo = productoService.guardar(dto);
        return ResponseEntity.status(201).body(nuevo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {

        return productoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (productoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

   
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorCategoria(
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(productoService.buscarPorCategoria(categoriaId));
    }

   
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorMarca(
            @PathVariable Long marcaId) {

        return ResponseEntity.ok(productoService.buscarPorMarca(marcaId));
    }

   
    @GetMapping("/precio")
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorPrecioMaximo(
            @RequestParam BigDecimal precioMax) {

        return ResponseEntity.ok(productoService.buscarPorPrecioMaximo(precioMax));
    }
}
