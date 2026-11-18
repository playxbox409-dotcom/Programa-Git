# Arquitectura web

## Flujo GET

```text
Navegador -> GET /animales -> AnimalServlet.doGet()
          -> AnimalServicio -> JdbcAnimalRepositorio -> MySQL
          -> request.setAttribute() -> JSP/JSTL -> HTML
```

## Flujo POST

```text
Formulario HTML -> POST /animales -> AnimalServlet.doPost()
                -> validación -> servicio -> repositorio JDBC
                -> redirect GET /animales -> mensaje de confirmación
```

El patrón POST/Redirect/GET evita que el navegador repita una inserción al actualizar la página. Las JSP se almacenan dentro de `WEB-INF`, por lo que solamente el Servlet puede mostrarlas mediante un `RequestDispatcher`.
