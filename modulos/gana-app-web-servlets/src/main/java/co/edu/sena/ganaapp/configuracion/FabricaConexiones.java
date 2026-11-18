package co.edu.sena.ganaapp.configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@FunctionalInterface
public interface FabricaConexiones {

    Connection abrir() throws SQLException;

    static FabricaConexiones desde(ConfiguracionBaseDatos configuracion) {
        return () -> DriverManager.getConnection(
                configuracion.url(),
                configuracion.usuario(),
                configuracion.clave()
        );
    }
}
