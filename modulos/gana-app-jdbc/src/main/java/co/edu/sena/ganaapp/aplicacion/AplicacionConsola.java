package co.edu.sena.ganaapp.aplicacion;

import co.edu.sena.ganaapp.configuracion.ConfiguracionBaseDatos;
import co.edu.sena.ganaapp.configuracion.FabricaConexiones;
import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.dominio.EstadoAnimal;
import co.edu.sena.ganaapp.repositorio.JdbcAnimalRepositorio;
import co.edu.sena.ganaapp.servicio.AnimalServicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public final class AplicacionConsola {

    private AplicacionConsola() {
    }

    public static void main(String[] args) {
        ConfiguracionBaseDatos configuracion = ConfiguracionBaseDatos.cargar();
        FabricaConexiones fabrica = FabricaConexiones.desde(configuracion);
        AnimalServicio servicio = new AnimalServicio(new JdbcAnimalRepositorio(fabrica));
        ejecutarMenu(servicio);
    }

    private static void ejecutarMenu(AnimalServicio servicio) {
        try (Scanner lector = new Scanner(System.in)) {
            int opcion;
            do {
                mostrarMenu();
                opcion = Integer.parseInt(lector.nextLine());
                switch (opcion) {
                    case 1 -> registrar(lector, servicio);
                    case 2 -> servicio.listar().forEach(System.out::println);
                    case 3 -> consultar(lector, servicio);
                    case 4 -> actualizar(lector, servicio);
                    case 5 -> eliminar(lector, servicio);
                    case 0 -> System.out.println("Aplicación finalizada.");
                    default -> System.out.println("Opción no válida.");
                }
            } while (opcion != 0);
        }
    }

    private static void mostrarMenu() {
        System.out.println("""

                GANA APP - MÓDULO DE ANIMALES
                1. Registrar animal
                2. Listar animales
                3. Consultar por id
                4. Actualizar precio y estado
                5. Eliminar animal
                0. Salir
                Seleccione una opción:
                """);
    }

    private static void registrar(Scanner lector, AnimalServicio servicio) {
        System.out.print("Id del propietario: ");
        long propietarioId = Long.parseLong(lector.nextLine());
        System.out.print("Código: ");
        String codigo = lector.nextLine();
        System.out.print("Nombre: ");
        String nombre = lector.nextLine();
        System.out.print("Especie: ");
        String especie = lector.nextLine();
        System.out.print("Raza: ");
        String raza = lector.nextLine();
        System.out.print("Peso en kg: ");
        BigDecimal peso = new BigDecimal(lector.nextLine());
        System.out.print("Precio en COP: ");
        BigDecimal precio = new BigDecimal(lector.nextLine());
        System.out.print("Ubicación: ");
        String ubicacion = lector.nextLine();
        Animal animal = new Animal(
                null, propietarioId, codigo, nombre, especie, raza,
                LocalDate.now(), peso, precio, ubicacion, EstadoAnimal.DISPONIBLE
        );
        System.out.println("Registrado: " + servicio.registrar(animal));
    }

    private static void consultar(Scanner lector, AnimalServicio servicio) {
        System.out.print("Id del animal: ");
        long id = Long.parseLong(lector.nextLine());
        System.out.println(servicio.consultar(id).map(Object::toString).orElse("No encontrado"));
    }

    private static void actualizar(Scanner lector, AnimalServicio servicio) {
        System.out.print("Id del animal: ");
        long id = Long.parseLong(lector.nextLine());
        Animal actual = servicio.consultar(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado"));
        System.out.print("Nuevo precio en COP: ");
        BigDecimal precio = new BigDecimal(lector.nextLine());
        System.out.print("Nuevo estado: ");
        EstadoAnimal estado = EstadoAnimal.valueOf(lector.nextLine().toUpperCase());
        Animal modificado = new Animal(
                actual.id(), actual.propietarioId(), actual.codigo(), actual.nombre(),
                actual.especie(), actual.raza(), actual.fechaNacimiento(), actual.pesoKg(),
                precio, actual.ubicacion(), estado
        );
        System.out.println(servicio.actualizar(modificado) ? "Actualizado." : "No encontrado.");
    }

    private static void eliminar(Scanner lector, AnimalServicio servicio) {
        System.out.print("Id del animal: ");
        long id = Long.parseLong(lector.nextLine());
        System.out.println(servicio.eliminar(id) ? "Eliminado." : "No encontrado.");
    }
}
