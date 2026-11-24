package co.edu.sena.ganaapp.servicio;

import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.dominio.EstadoAnimal;
import co.edu.sena.ganaapp.repositorio.AnimalRepositorio;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnimalServicioValidacionTest {

    private final AnimalServicio servicio = new AnimalServicio(new RepositorioPrueba());

    @Test
    void aceptaDatosValidosYCaracteresEspecialesEnNombre() {
        Animal animal = animalValido("GNA-001", "Muñeca Ñ", LocalDate.now().minusYears(2),
                new BigDecimal("450.50"), new BigDecimal("2600000"));
        assertDoesNotThrow(() -> servicio.registrar(animal));
    }

    @Test
    void rechazaFechaFutura() {
        Animal animal = animalValido("GNA-002", "Luna", LocalDate.now().plusDays(1),
                new BigDecimal("450"), new BigDecimal("2600000"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(animal));
    }

    @Test
    void rechazaPesoCeroONegativo() {
        Animal animal = animalValido("GNA-003", "Luna", LocalDate.now().minusYears(2),
                BigDecimal.ZERO, new BigDecimal("2600000"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(animal));
    }

    @Test
    void rechazaPrecioNegativo() {
        Animal animal = animalValido("GNA-004", "Luna", LocalDate.now().minusYears(2),
                new BigDecimal("450"), new BigDecimal("-1"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(animal));
    }

    @Test
    void rechazaCodigoConCaracteresNoPermitidos() {
        Animal animal = animalValido("GNA@005", "Luna", LocalDate.now().minusYears(2),
                new BigDecimal("450"), new BigDecimal("2600000"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(animal));
    }

    @Test
    void rechazaNombreDemasiadoCorto() {
        Animal animal = animalValido("GNA-006", "A", LocalDate.now().minusYears(2),
                new BigDecimal("450"), new BigDecimal("2600000"));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(animal));
    }

    private Animal animalValido(
            String codigo,
            String nombre,
            LocalDate fecha,
            BigDecimal peso,
            BigDecimal precio
    ) {
        return new Animal(null, 1, codigo, nombre, "BOVINO", "Cebú", fecha,
                peso, precio, "Bogotá D.C.", EstadoAnimal.DISPONIBLE);
    }

    private static final class RepositorioPrueba implements AnimalRepositorio {
        @Override public Animal crear(Animal animal) { return animal.conId(1); }
        @Override public Optional<Animal> buscarPorId(long id) { return Optional.empty(); }
        @Override public List<Animal> listarTodos() { return List.of(); }
        @Override public boolean actualizar(Animal animal) { return true; }
        @Override public boolean eliminar(long id) { return true; }
    }
}
