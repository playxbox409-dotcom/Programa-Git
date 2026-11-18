package co.edu.sena.ganaapp.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Animal(
        Long id,
        long propietarioId,
        String codigo,
        String nombre,
        String especie,
        String raza,
        LocalDate fechaNacimiento,
        BigDecimal pesoKg,
        BigDecimal precioCop,
        String ubicacion,
        EstadoAnimal estado
) {

    public Animal conId(long nuevoId) {
        return new Animal(
                nuevoId,
                propietarioId,
                codigo,
                nombre,
                especie,
                raza,
                fechaNacimiento,
                pesoKg,
                precioCop,
                ubicacion,
                estado
        );
    }
}
