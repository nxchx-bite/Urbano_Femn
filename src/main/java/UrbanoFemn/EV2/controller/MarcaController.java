package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(marcaService.obtenerTodas());
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return marcaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<MarcaResponseDTO> crear(
            @Valid @RequestBody MarcaRequestDTO dto) {

        MarcaResponseDTO nueva = marcaService.guardar(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO dto) {

        return marcaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (marcaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    public ResponseEntity<List<MarcaResponseDTO>> buscarActivas() {
        return ResponseEntity.ok(marcaService.buscarActivas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MarcaResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        return ResponseEntity.ok(marcaService.buscarPorNombre(nombre));
    }
}
