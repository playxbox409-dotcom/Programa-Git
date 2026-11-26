package co.edu.sena.ganaapp.servicio;

import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.repositorio.AnimalRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Aplica las validaciones del inventario antes de invocar la persistencia JDBC.
 * Esta separación evita que la interfaz y el repositorio dupliquen reglas.
 */
public final class AnimalServicio {

    private final AnimalRepositorio repositorio;

    public AnimalServicio(AnimalRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Animal registrar(Animal animal) {
        validar(animal, false);
        return repositorio.crear(animal);
    }

    public Optional<Animal> consultar(long id) {
        validarId(id);
        return repositorio.buscarPorId(id);
    }

    public List<Animal> listar() {
        return repositorio.listarTodos();
    }

    public boolean actualizar(Animal animal) {
        validar(animal, true);
        return repositorio.actualizar(animal);
    }

    public boolean eliminar(long id) {
        validarId(id);
        return repositorio.eliminar(id);
    }

    private void validar(Animal animal, boolean requiereId) {
        if (animal == null) {
            throw new IllegalArgumentException("El animal es obligatorio");
        }
        if (requiereId && (animal.id() == null || animal.id() <= 0)) {
            throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        }
        if (animal.propietarioId() <= 0) {
            throw new IllegalArgumentException("El propietario es obligatorio");
        }
        validarTexto(animal.codigo(), "código");
        validarLongitud(animal.codigo(), "código", 3, 30);
        if (!animal.codigo().matches("[A-Za-z0-9-]+")) {
            throw new IllegalArgumentException("El código solo admite letras, números y guiones");
        }
        validarTexto(animal.nombre(), "nombre");
        validarLongitud(animal.nombre(), "nombre", 2, 100);
        validarTexto(animal.especie(), "especie");
        validarTexto(animal.raza(), "raza");
        validarLongitud(animal.raza(), "raza", 2, 80);
        validarTexto(animal.ubicacion(), "ubicación");
        validarLongitud(animal.ubicacion(), "ubicación", 2, 120);
        if (animal.fechaNacimiento() != null && animal.fechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        validarPositivo(animal.pesoKg(), "peso");
        if (animal.precioCop() == null || animal.precioCop().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (animal.estado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
    }

    private void validarId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El identificador debe ser mayor que cero");
        }
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio");
        }
    }

    private void validarPositivo(BigDecimal valor, String campo) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El " + campo + " debe ser mayor que cero");
        }
    }

    private void validarLongitud(String valor, String campo, int minimo, int maximo) {
        if (valor.length() < minimo || valor.length() > maximo) {
            throw new IllegalArgumentException(
                    "El " + campo + " debe tener entre " + minimo + " y " + maximo + " caracteres"
            );
        }
    }
}
