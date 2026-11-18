package co.edu.sena.ganaapp.web;

import co.edu.sena.ganaapp.configuracion.ConfiguracionBaseDatos;
import co.edu.sena.ganaapp.configuracion.FabricaConexiones;
import co.edu.sena.ganaapp.repositorio.JdbcAnimalRepositorio;
import co.edu.sena.ganaapp.servicio.AnimalServicio;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public final class InicializadorAplicacion implements ServletContextListener {

    public static final String ATRIBUTO_SERVICIO = "animalServicio";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        ConfiguracionBaseDatos configuracion = ConfiguracionBaseDatos.cargar();
        FabricaConexiones fabrica = FabricaConexiones.desde(configuracion);
        AnimalServicio servicio = new AnimalServicio(new JdbcAnimalRepositorio(fabrica));
        evento.getServletContext().setAttribute(ATRIBUTO_SERVICIO, servicio);
    }
}
