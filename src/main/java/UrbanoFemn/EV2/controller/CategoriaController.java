package UrbanoFemn.EV2.controller;
<<<<<<< HEAD
import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

  
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        CategoriaResponseDTO nueva = categoriaService.guardar(dto);
        return ResponseEntity.status(201).body(nueva);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        return categoriaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (categoriaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaResponseDTO>> buscarActivas() {
        return ResponseEntity.ok(categoriaService.buscarActivas());
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
    }
=======

public class CategoriaController {

>>>>>>> 1e8e949bda6fe594fb13ae0fa5e785a168c0eb46
}
