package co.edu.sena.ganaapp.web;

import co.edu.sena.ganaapp.configuracion.ConfiguracionBaseDatos;
import co.edu.sena.ganaapp.configuracion.FabricaConexiones;
import co.edu.sena.ganaapp.repositorio.JdbcAnimalRepositorio;
import co.edu.sena.ganaapp.servicio.AnimalServicio;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public final class InicializadorAplicacion implements ServletContextListener {

    public static final String ATRIBUTO_SERVICIO = "animalServicio";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ConfiguracionBaseDatos configuracion = ConfiguracionBaseDatos.cargar();
        FabricaConexiones fabrica = FabricaConexiones.desde(configuracion);
        inicializarDemostracionH2(configuracion, fabrica);
        AnimalServicio servicio = new AnimalServicio(new JdbcAnimalRepositorio(fabrica));
        evento.getServletContext().setAttribute(ATRIBUTO_SERVICIO, servicio);
    }

    private void inicializarDemostracionH2(
            ConfiguracionBaseDatos configuracion,
            FabricaConexiones fabrica
    ) {
        if (!configuracion.url().startsWith("jdbc:h2:")) {
            return;
        }
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException excepcion) {
            throw new IllegalStateException("No se encontró el controlador H2", excepcion);
        }
        try (InputStream entrada = getClass().getClassLoader().getResourceAsStream("db/schema-h2.sql")) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró el esquema H2 de demostración");
            }
            String sql = new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            try (Connection conexion = fabrica.abrir(); Statement sentencia = conexion.createStatement()) {
                for (String instruccion : sql.split(";")) {
                    if (!instruccion.isBlank()) {
                        sentencia.execute(instruccion);
                    }
                }
            }
        } catch (IOException | SQLException excepcion) {
            throw new IllegalStateException("No fue posible inicializar la base de demostración", excepcion);
        }
    }
}
