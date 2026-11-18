# GANA APP - Módulo web con Servlets y JSP

Aplicación web Java para administrar el inventario ganadero. El módulo utiliza formularios HTML, Servlets, JSP, JSTL y JDBC.

## Funcionalidades

- Listar animales mediante una petición GET.
- Mostrar formularios de registro y edición mediante GET.
- Registrar animales mediante POST.
- Actualizar animales mediante POST.
- Eliminar animales mediante POST.
- Validar datos antes de ejecutar operaciones JDBC.
- Mostrar mensajes de confirmación y errores en JSP.

## Arquitectura

```text
Navegador -> AnimalServlet -> AnimalServicio -> JdbcAnimalRepositorio -> MySQL
                 |
                 +-> JSP + JSTL -> HTML
```

## Requisitos

- Java 21 o superior.
- Maven 3.9 o superior.
- MySQL 8 o superior.
- Tomcat 11, compatible con Jakarta Servlet 6.1 y JSP 4.0.

## Ejecución

1. Ejecute `src/main/resources/db/schema-mysql.sql` en MySQL.
2. Configure `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. Ejecute `mvn clean test package`.
4. Copie `target/gana-app-web.war` en la carpeta `webapps` de Tomcat.
5. Abra `http://localhost:8080/gana-app-web/`.

## Rutas

| Método | Ruta | Acción |
|---|---|---|
| GET | `/animales` | Listar animales |
| GET | `/animales?accion=nuevo` | Mostrar formulario de registro |
| GET | `/animales?accion=editar&id=1` | Mostrar formulario de edición |
| POST | `/animales` con `accion=crear` | Insertar |
| POST | `/animales` con `accion=actualizar` | Actualizar |
| POST | `/animales` con `accion=eliminar` | Eliminar |

## Pruebas

`mvn clean test` ejecuta una prueba de integración JDBC en H2, configurada en modo compatible con MySQL. La prueba verifica inserción, consulta, listado, actualización y eliminación.
