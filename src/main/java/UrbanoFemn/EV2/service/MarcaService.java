package UrbanoFemn.EV2.service;

import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MarcaService {
    
    private final MarcaRepository marcaRepository;

    private MarcaResponseDTO mapToDTO(Marca marca) {
        return new MarcaResponseDTO(
                marca.getId(),
                marca.getNombre(),
                marca.getDescripcion(),
                marca.getPaisOrigen(),
                marca.getActiva()
        );
    }

    public List<MarcaResponseDTO> obtenerTodas() {
        log.info("Listando todas las marcas");

        return marcaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<MarcaResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando marca con ID: {}", id);

        return marcaRepository.findById(id)
                .map(this::mapToDTO);
    }

    public MarcaResponseDTO guardar(MarcaRequestDTO dto) {
        log.info("Creando marca: {}", dto.getNombre());

        Marca marca = new Marca(
                null,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPaisOrigen(),
                dto.getActiva()
        );

        Marca guardada = marcaRepository.save(marca);

        log.info("Marca creada correctamente con ID: {}", guardada.getId());

        return mapToDTO(guardada);
    }

    public Optional<MarcaResponseDTO> actualizar(Long id, MarcaRequestDTO dto) {
        log.info("Actualizando marca con ID: {}", id);

        return marcaRepository.findById(id).map(existente -> {
            existente.setNombre(dto.getNombre());
            existente.setDescripcion(dto.getDescripcion());
            existente.setPaisOrigen(dto.getPaisOrigen());
            existente.setActiva(dto.getActiva());

            Marca actualizada = marcaRepository.save(existente);

            log.info("Marca actualizada correctamente con ID: {}", actualizada.getId());

            return mapToDTO(actualizada);
        });
    }

    public void eliminar(Long id) {
        log.warn("Eliminando marca con ID: {}", id);
        marcaRepository.deleteById(id);
    }

    public List<MarcaResponseDTO> buscarActivas() {
        log.info("Buscando marcas activas");

        return marcaRepository.findByActivaTrue()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<MarcaResponseDTO> buscarPorNombre(String nombre) {
        log.info("Buscando marcas por nombre: {}", nombre);

        return marcaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
