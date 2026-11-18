package co.edu.sena.ganaapp.configuracion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public record ConfiguracionBaseDatos(String url, String usuario, String clave) {

    private static final String ARCHIVO_PROPIEDADES = "application.properties";

    public ConfiguracionBaseDatos {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL JDBC es obligatoria");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario de base de datos es obligatorio");
        }
        clave = clave == null ? "" : clave;
    }

    public static ConfiguracionBaseDatos cargar() {
        Properties propiedades = cargarPropiedades();
        String url = leerVariable("DB_URL", propiedades.getProperty("db.url"));
        String usuario = leerVariable("DB_USER", propiedades.getProperty("db.user"));
        String clave = leerVariable("DB_PASSWORD", propiedades.getProperty("db.password", ""));
        return new ConfiguracionBaseDatos(url, usuario, clave);
    }

    private static Properties cargarPropiedades() {
        Properties propiedades = new Properties();
        try (InputStream entrada = ConfiguracionBaseDatos.class.getClassLoader()
                .getResourceAsStream(ARCHIVO_PROPIEDADES)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró " + ARCHIVO_PROPIEDADES);
            }
            propiedades.load(entrada);
            return propiedades;
        } catch (IOException excepcion) {
            throw new IllegalStateException("No fue posible leer la configuración", excepcion);
        }
    }

    private static String leerVariable(String nombre, String valorPredeterminado) {
        String valor = System.getenv(nombre);
        return valor == null || valor.isBlank() ? valorPredeterminado : valor;
    }
}
