package co.edu.sena.ganaapp.servicio;

import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.repositorio.AnimalRepositorio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        validarTexto(animal.nombre(), "nombre");
        validarTexto(animal.especie(), "especie");
        validarTexto(animal.raza(), "raza");
        validarTexto(animal.ubicacion(), "ubicación");
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
}
