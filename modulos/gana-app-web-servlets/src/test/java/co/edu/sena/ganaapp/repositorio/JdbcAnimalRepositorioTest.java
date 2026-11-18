package co.edu.sena.ganaapp.repositorio;

import co.edu.sena.ganaapp.configuracion.FabricaConexiones;
import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.dominio.EstadoAnimal;
import co.edu.sena.ganaapp.servicio.AnimalServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcAnimalRepositorioTest {

    private static final String URL = "jdbc:h2:mem:gana_app;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private AnimalServicio servicio;

    @BeforeEach
    void prepararBaseDatos() throws SQLException, IOException {
        FabricaConexiones fabrica = () -> DriverManager.getConnection(URL, "sa", "");
        ejecutarEsquema(fabrica);
        servicio = new AnimalServicio(new JdbcAnimalRepositorio(fabrica));
    }

    @Test
    void debeEjecutarCrudCompletoConJdbc() {
        Animal nuevo = crearAnimal();

        Animal creado = servicio.registrar(nuevo);
        assertTrue(creado.id() > 0);
        assertEquals("GNA-001", servicio.consultar(creado.id()).orElseThrow().codigo());
        assertEquals(1, servicio.listar().size());

        Animal actualizado = new Animal(
                creado.id(), creado.propietarioId(), creado.codigo(), creado.nombre(),
                creado.especie(), creado.raza(), creado.fechaNacimiento(),
                new BigDecimal("460.50"), new BigDecimal("3200000"),
                "Montería", EstadoAnimal.RESERVADO
        );
        assertTrue(servicio.actualizar(actualizado));
        Animal consultado = servicio.consultar(creado.id()).orElseThrow();
        assertEquals(new BigDecimal("3200000.00"), consultado.precioCop());
        assertEquals(EstadoAnimal.RESERVADO, consultado.estado());

        assertTrue(servicio.eliminar(creado.id()));
        assertFalse(servicio.consultar(creado.id()).isPresent());
        assertTrue(servicio.listar().isEmpty());
    }

    private Animal crearAnimal() {
        return new Animal(
                null,
                1,
                "GNA-001",
                "Luna",
                "BOVINO",
                "Brahman",
                LocalDate.of(2024, 5, 10),
                new BigDecimal("450.00"),
                new BigDecimal("2900000"),
                "Montería",
                EstadoAnimal.DISPONIBLE
        );
    }

    private void ejecutarEsquema(FabricaConexiones fabrica) throws SQLException, IOException {
        String recurso = "/db/schema-h2.sql";
        String sql;
        try (var entrada = getClass().getResourceAsStream(recurso)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró " + recurso);
            }
            sql = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        }
        try (Connection conexion = fabrica.abrir(); Statement sentencia = conexion.createStatement()) {
            for (String fragmento : sql.split(";")) {
                if (!fragmento.isBlank()) {
                    sentencia.execute(fragmento);
                }
            }
        }
    }
}
