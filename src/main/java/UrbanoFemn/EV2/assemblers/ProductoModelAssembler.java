package UrbanoFemn.EV2.assemblers;

import UrbanoFemn.EV2.controller.ProductoControllerV2;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<ProductoResponseDTO, EntityModel<ProductoResponseDTO>> {

    @Override
    public EntityModel<ProductoResponseDTO> toModel(ProductoResponseDTO producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"),
                linkTo(methodOn(ProductoControllerV2.class).buscarPorNombre(producto.getNombre())).withRel("buscar-por-nombre"),
                linkTo(methodOn(ProductoControllerV2.class).buscarPorPrecioMaximo(producto.getPrecio())).withRel("buscar-por-precio")
        );
    }
}