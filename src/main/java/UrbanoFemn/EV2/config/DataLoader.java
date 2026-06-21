package UrbanoFemn.EV2.config;

import UrbanoFemn.EV2.model.Categoria;
import UrbanoFemn.EV2.model.Marca;
import UrbanoFemn.EV2.model.Producto;
import UrbanoFemn.EV2.repository.CategoriaRepository;
import UrbanoFemn.EV2.repository.MarcaRepository;
import UrbanoFemn.EV2.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("La base de datos ya contiene productos. Se omite la carga con DataFaker.");
            return;
        }

        Faker faker = new Faker(new Locale("es"));
        Random random = new Random();

        log.info("Iniciando carga de datos falsos con DataFaker...");

        crearCategorias();
        crearMarcas(faker);
        crearProductos(faker, random);

        log.info("Carga de datos falsos completada correctamente.");
    }

    private void crearCategorias() {
        List<Categoria> categorias = List.of(
                new Categoria(null, "Poleras", "Poleras femeninas urbanas", true),
                new Categoria(null, "Pantalones", "Pantalones de estilo casual y urbano", true),
                new Categoria(null, "Vestidos", "Vestidos para uso diario y ocasiones especiales", true),
                new Categoria(null, "Chaquetas", "Chaquetas y abrigos femeninos", true),
                new Categoria(null, "Accesorios", "Accesorios de moda femenina", true)
        );

        categoriaRepository.saveAll(categorias);
    }

    private void crearMarcas(Faker faker) {
        List<Marca> marcas = List.of(
                new Marca(null, "Urbano Femn", "Marca principal de la tienda", "Chile", true),
                new Marca(null, faker.company().name(), "Marca internacional de moda", "España", true),
                new Marca(null, faker.company().name(), "Marca juvenil de ropa urbana", "Argentina", true),
                new Marca(null, faker.company().name(), "Marca importada de ropa femenina", "Colombia", true),
                new Marca(null, faker.company().name(), "Marca de accesorios femeninos", "México", true)
        );

        marcaRepository.saveAll(marcas);
    }

    private void crearProductos(Faker faker, Random random) {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<Marca> marcas = marcaRepository.findAll();

        String[] tiposProducto = {
                "Polera Oversize",
                "Jeans Wide Leg",
                "Vestido Casual",
                "Chaqueta Denim",
                "Top Básico",
                "Pantalón Cargo",
                "Falda Urbana",
                "Blusa Elegante",
                "Polerón Crop",
                "Short Casual"
        };

        String[] colores = {
                "Negro",
                "Blanco",
                "Rosado",
                "Beige",
                "Azul",
                "Verde",
                "Gris"
        };

        String[] tallas = {
                "XS",
                "S",
                "M",
                "L",
                "XL"
        };

        for (int i = 0; i < 30; i++) {
            String nombre = tiposProducto[random.nextInt(tiposProducto.length)] + " " + colores[random.nextInt(colores.length)];

            BigDecimal precio = BigDecimal.valueOf(faker.number().numberBetween(7990, 49990));
            Integer stock = faker.number().numberBetween(5, 80);
            String talla = tallas[random.nextInt(tallas.length)];
            String color = colores[random.nextInt(colores.length)];

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(faker.lorem().sentence(10));
            producto.setPrecio(precio);
            producto.setStock(stock);
            producto.setTalla(talla);
            producto.setColor(color);
            producto.setActivo(true);
            producto.setCategoria(categorias.get(random.nextInt(categorias.size())));
            producto.setMarca(marcas.get(random.nextInt(marcas.size())));

            productoRepository.save(producto);
        }
    }
}

