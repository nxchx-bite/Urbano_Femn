package UrbanoFemn.EV2.service;

import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    private CategoriaResponseDTO mapToDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getActiva()
        );
    }

    public List<CategoriaResponseDTO> obtenerTodas() {
        log.info("Listando todas las categorías");

        return categoriaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoriaResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando categoría con ID: {}", id);

        return categoriaRepository.findById(id)
                .map(this::mapToDTO);
    }

    public CategoriaResponseDTO guardar(CategoriaRequestDTO dto) {
        log.info("Creando categoría: {}", dto.getNombre());

        Categoria categoria = new Categoria(
                null,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getActiva()
        );

        Categoria guardada = categoriaRepository.save(categoria);

        log.info("Categoría creada correctamente con ID: {}", guardada.getId());

        return mapToDTO(guardada);
    }

    public Optional<CategoriaResponseDTO> actualizar(Long id, CategoriaRequestDTO dto) {
        log.info("Actualizando categoría con ID: {}", id);

        return categoriaRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            existente.setDescripcion(dto.getDescripcion());
            existente.setActiva(dto.getActiva());

            Categoria actualizada = categoriaRepository.save(existente);

            log.info("Categoría actualizada correctamente con ID: {}", actualizada.getId());

            return mapToDTO(actualizada);
        });
    }

    public void eliminar(Long id) {
        log.warn("Eliminando categoría con ID: {}", id);
        categoriaRepository.deleteById(id);
    }

    public List<CategoriaResponseDTO> buscarActivas() {
        log.info("Buscando categorías activas");

        return categoriaRepository.findByActivaTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando categorías por nombre: {}", nombre);

        return categoriaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }   
}
