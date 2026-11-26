package co.edu.sena.ganaapp.web;

import co.edu.sena.ganaapp.dominio.Animal;
import co.edu.sena.ganaapp.dominio.EstadoAnimal;
import co.edu.sena.ganaapp.servicio.AnimalServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controlador web del módulo de animales. Atiende las solicitudes GET y POST,
 * delega las reglas de negocio al servicio y selecciona la vista JSP adecuada.
 */
@WebServlet(name = "animalServlet", urlPatterns = "/animales")
public final class AnimalServlet extends HttpServlet {

    private static final String VISTA_LISTA = "/WEB-INF/vistas/animales/lista.jsp";
    private static final String VISTA_FORMULARIO = "/WEB-INF/vistas/animales/formulario.jsp";

    private transient AnimalServicio servicio;

    @Override
    public void init() throws ServletException {
        Object atributo = getServletContext().getAttribute(InicializadorAplicacion.ATRIBUTO_SERVICIO);
        if (!(atributo instanceof AnimalServicio animalServicio)) {
            throw new ServletException("El servicio de animales no fue inicializado");
        }
        servicio = animalServicio;
    }

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        solicitud.setCharacterEncoding("UTF-8");
        respuesta.setCharacterEncoding("UTF-8");
        respuesta.setContentType("text/html;charset=UTF-8");
        String accion = parametro(solicitud, "accion", "listar");
        try {
            switch (accion) {
                case "nuevo" -> mostrarFormularioNuevo(solicitud, respuesta);
                case "editar" -> mostrarFormularioEdicion(solicitud, respuesta);
                default -> listar(solicitud, respuesta);
            }
        } catch (IllegalArgumentException excepcion) {
            mostrarError(solicitud, respuesta, excepcion.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        solicitud.setCharacterEncoding("UTF-8");
        respuesta.setCharacterEncoding("UTF-8");
        respuesta.setContentType("text/html;charset=UTF-8");
        String accion = parametro(solicitud, "accion", "crear");
        try {
            switch (accion) {
                case "crear" -> crear(solicitud, respuesta);
                case "actualizar" -> actualizar(solicitud, respuesta);
                case "eliminar" -> eliminar(solicitud, respuesta);
                default -> throw new IllegalArgumentException("La acción solicitada no es válida");
            }
        } catch (IllegalArgumentException excepcion) {
            solicitud.setAttribute("error", excepcion.getMessage());
            solicitud.setAttribute("modo", "actualizar".equals(accion) ? "actualizar" : "crear");
            solicitud.getRequestDispatcher(VISTA_FORMULARIO).forward(solicitud, respuesta);
        }
    }

    private void listar(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        solicitud.setAttribute("animales", servicio.listar());
        solicitud.getRequestDispatcher(VISTA_LISTA).forward(solicitud, respuesta);
    }

    private void mostrarFormularioNuevo(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        solicitud.setAttribute("modo", "crear");
        solicitud.getRequestDispatcher(VISTA_FORMULARIO).forward(solicitud, respuesta);
    }

    private void mostrarFormularioEdicion(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        long id = leerLong(solicitud, "id");
        Animal animal = servicio.consultar(id)
                .orElseThrow(() -> new IllegalArgumentException("El animal solicitado no existe"));
        solicitud.setAttribute("animal", animal);
        solicitud.setAttribute("modo", "actualizar");
        solicitud.getRequestDispatcher(VISTA_FORMULARIO).forward(solicitud, respuesta);
    }

    private void crear(HttpServletRequest solicitud, HttpServletResponse respuesta) throws IOException {
        Animal creado = servicio.registrar(construirAnimal(solicitud, false));
        redirigirConMensaje(solicitud, respuesta, "creado", creado.id());
    }

    private void actualizar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws IOException {
        Animal animal = construirAnimal(solicitud, true);
        if (!servicio.actualizar(animal)) {
            throw new IllegalArgumentException("El animal no existe o no pudo actualizarse");
        }
        redirigirConMensaje(solicitud, respuesta, "actualizado", animal.id());
    }

    private void eliminar(HttpServletRequest solicitud, HttpServletResponse respuesta) throws IOException {
        long id = leerLong(solicitud, "id");
        if (!servicio.eliminar(id)) {
            throw new IllegalArgumentException("El animal no existe o ya fue eliminado");
        }
        redirigirConMensaje(solicitud, respuesta, "eliminado", id);
    }

    private Animal construirAnimal(HttpServletRequest solicitud, boolean requiereId) {
        Long id = requiereId ? leerLong(solicitud, "id") : null;
        String fechaTexto = parametro(solicitud, "fechaNacimiento", "");
        LocalDate fecha = fechaTexto.isBlank() ? null : LocalDate.parse(fechaTexto);
        return new Animal(
                id,
                leerLong(solicitud, "propietarioId"),
                parametroObligatorio(solicitud, "codigo"),
                parametroObligatorio(solicitud, "nombre"),
                parametroObligatorio(solicitud, "especie"),
                parametroObligatorio(solicitud, "raza"),
                fecha,
                leerDecimal(solicitud, "pesoKg"),
                leerDecimal(solicitud, "precioCop"),
                parametroObligatorio(solicitud, "ubicacion"),
                EstadoAnimal.valueOf(parametro(solicitud, "estado", "DISPONIBLE").toUpperCase())
        );
    }

    private long leerLong(HttpServletRequest solicitud, String nombre) {
        try {
            return Long.parseLong(parametroObligatorio(solicitud, nombre));
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException("El campo " + nombre + " debe ser numérico", excepcion);
        }
    }

    private BigDecimal leerDecimal(HttpServletRequest solicitud, String nombre) {
        try {
            return new BigDecimal(parametroObligatorio(solicitud, nombre));
        } catch (NumberFormatException excepcion) {
            throw new IllegalArgumentException("El campo " + nombre + " debe ser numérico", excepcion);
        }
    }

    private String parametroObligatorio(HttpServletRequest solicitud, String nombre) {
        String valor = solicitud.getParameter(nombre);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombre + " es obligatorio");
        }
        return valor.trim();
    }

    private String parametro(HttpServletRequest solicitud, String nombre, String valorPredeterminado) {
        String valor = solicitud.getParameter(nombre);
        return valor == null || valor.isBlank() ? valorPredeterminado : valor.trim();
    }

    private void redirigirConMensaje(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            String mensaje,
            long id
    ) throws IOException {
        respuesta.sendRedirect(
                solicitud.getContextPath() + "/animales?mensaje=" + mensaje + "&id=" + id
        );
    }

    private void mostrarError(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            String mensaje
    ) throws ServletException, IOException {
        solicitud.setAttribute("error", mensaje);
        listar(solicitud, respuesta);
    }
}
