package co.edu.sena.ganaapp.repositorio;

import co.edu.sena.ganaapp.dominio.Animal;

import java.util.List;
import java.util.Optional;

public interface AnimalRepositorio {

    Animal crear(Animal animal);

    Optional<Animal> buscarPorId(long id);

    List<Animal> listarTodos();

    boolean actualizar(Animal animal);

    boolean eliminar(long id);
}
