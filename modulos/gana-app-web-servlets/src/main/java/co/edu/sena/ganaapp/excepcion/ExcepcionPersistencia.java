package co.edu.sena.ganaapp.excepcion;

public class ExcepcionPersistencia extends RuntimeException {

    public ExcepcionPersistencia(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
