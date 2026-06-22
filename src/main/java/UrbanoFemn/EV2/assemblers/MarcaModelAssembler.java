package UrbanoFemn.EV2.assemblers;

import UrbanoFemn.EV2.controller.MarcaControllerV2;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MarcaModelAssembler implements RepresentationModelAssembler<MarcaResponseDTO, EntityModel<MarcaResponseDTO>> {

    @Override
    public EntityModel<MarcaResponseDTO> toModel(MarcaResponseDTO marca) {
        return EntityModel.of(marca,
                linkTo(methodOn(MarcaControllerV2.class).obtenerPorId(marca.getId())).withSelfRel(),
                linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withRel("marcas"),
                linkTo(methodOn(MarcaControllerV2.class).buscarActivas()).withRel("marcas-activas"),
                linkTo(methodOn(MarcaControllerV2.class).buscarPorNombre(marca.getNombre())).withRel("buscar-por-nombre")
        );
    }
}