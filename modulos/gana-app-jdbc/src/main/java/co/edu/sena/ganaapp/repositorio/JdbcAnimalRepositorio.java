package co.edu.sena.ganaapp.repositorio;

import co.edu.sena.ganaapp.configuracion.FabricaConexiones;
import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.dominio.EstadoAnimal;
import co.edu.sena.ganaapp.excepcion.ExcepcionPersistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcAnimalRepositorio implements AnimalRepositorio {

    private static final String SQL_INSERTAR = """
            INSERT INTO animales (
                id_propietario, codigo, nombre, especie, raza, fecha_nacimiento,
                peso_kg, precio_cop, ubicacion, estado
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_BUSCAR_POR_ID = """
            SELECT id_animal, id_propietario, codigo, nombre, especie, raza,
                   fecha_nacimiento, peso_kg, precio_cop, ubicacion, estado
              FROM animales
             WHERE id_animal = ?
            """;

    private static final String SQL_LISTAR = """
            SELECT id_animal, id_propietario, codigo, nombre, especie, raza,
                   fecha_nacimiento, peso_kg, precio_cop, ubicacion, estado
              FROM animales
             ORDER BY id_animal
            """;

    private static final String SQL_ACTUALIZAR = """
            UPDATE animales
               SET id_propietario = ?, codigo = ?, nombre = ?, especie = ?, raza = ?,
                   fecha_nacimiento = ?, peso_kg = ?, precio_cop = ?, ubicacion = ?,
                   estado = ?, actualizado_en = CURRENT_TIMESTAMP
             WHERE id_animal = ?
            """;

    private static final String SQL_ELIMINAR = "DELETE FROM animales WHERE id_animal = ?";

    private final FabricaConexiones fabricaConexiones;

    public JdbcAnimalRepositorio(FabricaConexiones fabricaConexiones) {
        this.fabricaConexiones = fabricaConexiones;
    }

    @Override
    public Animal crear(Animal animal) {
        return ejecutarEnTransaccion(conexion -> {
            try (PreparedStatement sentencia = conexion.prepareStatement(
                    SQL_INSERTAR,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                asignarParametros(sentencia, animal);
                if (sentencia.executeUpdate() != 1) {
                    throw new SQLException("La inserción no afectó una fila");
                }
                try (ResultSet claves = sentencia.getGeneratedKeys()) {
                    if (!claves.next()) {
                        throw new SQLException("No se obtuvo el identificador generado");
                    }
                    return animal.conId(claves.getLong(1));
                }
            }
        }, "No fue posible crear el animal");
    }

    @Override
    public Optional<Animal> buscarPorId(long id) {
        try (Connection conexion = fabricaConexiones.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_ID)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException excepcion) {
            throw new ExcepcionPersistencia("No fue posible consultar el animal", excepcion);
        }
    }

    @Override
    public List<Animal> listarTodos() {
        List<Animal> animales = new ArrayList<>();
        try (Connection conexion = fabricaConexiones.abrir();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_LISTAR);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                animales.add(mapear(resultado));
            }
            return List.copyOf(animales);
        } catch (SQLException excepcion) {
            throw new ExcepcionPersistencia("No fue posible listar los animales", excepcion);
        }
    }

    @Override
    public boolean actualizar(Animal animal) {
        if (animal.id() == null) {
            throw new IllegalArgumentException("El identificador es obligatorio para actualizar");
        }
        return ejecutarEnTransaccion(conexion -> {
            try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
                asignarParametros(sentencia, animal);
                sentencia.setLong(11, animal.id());
                return sentencia.executeUpdate() == 1;
            }
        }, "No fue posible actualizar el animal");
    }

    @Override
    public boolean eliminar(long id) {
        return ejecutarEnTransaccion(conexion -> {
            try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
                sentencia.setLong(1, id);
                return sentencia.executeUpdate() == 1;
            }
        }, "No fue posible eliminar el animal");
    }

    private void asignarParametros(PreparedStatement sentencia, Animal animal) throws SQLException {
        sentencia.setLong(1, animal.propietarioId());
        sentencia.setString(2, animal.codigo());
        sentencia.setString(3, animal.nombre());
        sentencia.setString(4, animal.especie());
        sentencia.setString(5, animal.raza());
        if (animal.fechaNacimiento() == null) {
            sentencia.setNull(6, Types.DATE);
        } else {
            sentencia.setDate(6, Date.valueOf(animal.fechaNacimiento()));
        }
        sentencia.setBigDecimal(7, animal.pesoKg());
        sentencia.setBigDecimal(8, animal.precioCop());
        sentencia.setString(9, animal.ubicacion());
        sentencia.setString(10, animal.estado().name());
    }

    private Animal mapear(ResultSet resultado) throws SQLException {
        Date fecha = resultado.getDate("fecha_nacimiento");
        return new Animal(
                resultado.getLong("id_animal"),
                resultado.getLong("id_propietario"),
                resultado.getString("codigo"),
                resultado.getString("nombre"),
                resultado.getString("especie"),
                resultado.getString("raza"),
                fecha == null ? null : fecha.toLocalDate(),
                resultado.getBigDecimal("peso_kg"),
                resultado.getBigDecimal("precio_cop"),
                resultado.getString("ubicacion"),
                EstadoAnimal.valueOf(resultado.getString("estado"))
        );
    }

    private <T> T ejecutarEnTransaccion(OperacionSql<T> operacion, String mensajeError) {
        try (Connection conexion = fabricaConexiones.abrir()) {
            boolean autoCommitOriginal = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                T resultado = operacion.ejecutar(conexion);
                conexion.commit();
                return resultado;
            } catch (SQLException | RuntimeException excepcion) {
                conexion.rollback();
                throw excepcion;
            } finally {
                conexion.setAutoCommit(autoCommitOriginal);
            }
        } catch (SQLException excepcion) {
            throw new ExcepcionPersistencia(mensajeError, excepcion);
        }
    }

    @FunctionalInterface
    private interface OperacionSql<T> {
        T ejecutar(Connection conexion) throws SQLException;
    }
}
