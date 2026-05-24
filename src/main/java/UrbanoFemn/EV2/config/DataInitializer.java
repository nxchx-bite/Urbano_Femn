package UrbanoFemn.EV2.config;

import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.model.Producto;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import UrbanoFemn.EV2.repository.MarcaRepository;
import UrbanoFemn.EV2.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;



@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) {

        if (categoriaRepository.count() > 0 || marcaRepository.count() > 0 || productoRepository.count() > 0) {
            log.info("La base de datos ya contiene datos. Se omite la carga inicial.");
            return;
        }

        log.info("Insertando datos iniciales de Graffity Glam...");

        Categoria poleras = categoriaRepository.save(
                new Categoria(null, "Poleras", "Poleras femeninas urbanas", true)
        );

        Categoria pantalones = categoriaRepository.save(
                new Categoria(null, "Pantalones", "Jeans y pantalones femeninos", true)
        );

        Categoria chaquetas = categoriaRepository.save(
                new Categoria(null, "Chaquetas", "Chaquetas y prendas de abrigo", true)
        );

      
        Marca shein = marcaRepository.save(
                new Marca(null, "Shein", "Marca de moda femenina", "China", true)
        );

        Marca zara = marcaRepository.save(
                new Marca(null, "Zara", "Marca internacional de ropa", "España", true)
        );

        Marca urbano = marcaRepository.save(
                new Marca(null, "Urbano Femn", "Marca propia de la tienda", "Chile", true)
        );

        productoRepository.save(new Producto(
                null,
                "Polera Oversize Negra",
                "Polera oversize de algodón estilo urbano",
                new BigDecimal("12990"),
                20,
                "M",
                "Negro",
                true,
                poleras,
                urbano
        ));

        productoRepository.save(new Producto(
                null,
                "Jeans Tiro Alto Azul",
                "Jeans femenino tiro alto color azul",
                new BigDecimal("24990"),
                15,
                "40",
                "Azul",
                true,
                pantalones,
                zara
        ));

        productoRepository.save(new Producto(
                null,
                "Chaqueta Denim",
                "Chaqueta de mezclilla para temporada urbana",
                new BigDecimal("29990"),
                10,
                "L",
                "Celeste",
                true,
                chaquetas,
                shein
        ));

        log.info("Datos iniciales insertados correctamente.");
        log.info("Categorías creadas: {}", categoriaRepository.count());
        log.info("Marcas creadas: {}", marcaRepository.count());
        log.info("Productos creados: {}", productoRepository.count());
    }
}
