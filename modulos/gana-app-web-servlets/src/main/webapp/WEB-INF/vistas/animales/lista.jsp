<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Animales | GANA APP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<%@ include file="../comun/cabecera.jspf" %>
<main class="contenedor">
    <section class="titulo-pagina">
        <div>
            <p class="etiqueta">Inventario ganadero</p>
            <h1>Animales registrados</h1>
            <p>Consulta, edita y elimina los animales del inventario.</p>
        </div>
        <a class="boton boton--primario" href="${pageContext.request.contextPath}/animales?accion=nuevo">
            Registrar animal
        </a>
    </section>

    <c:if test="${not empty param.mensaje}">
        <div class="alerta alerta--exito" role="status">
            Operación completada: <c:out value="${param.mensaje}" />.
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alerta alerta--error" role="alert"><c:out value="${error}" /></div>
    </c:if>

    <c:choose>
        <c:when test="${empty animales}">
            <section class="vacio">
                <span aria-hidden="true">🐮</span>
                <h2>No hay animales registrados</h2>
                <p>Registra el primer animal para iniciar el inventario.</p>
            </section>
        </c:when>
        <c:otherwise>
            <div class="tabla-contenedor">
                <table>
                    <thead>
                    <tr>
                        <th>Código</th><th>Animal</th><th>Raza</th><th>Peso</th>
                        <th>Precio</th><th>Estado</th><th>Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${animales}" var="animal">
                        <tr>
                            <td><c:out value="${animal.codigo}" /></td>
                            <td>
                                <strong><c:out value="${animal.nombre}" /></strong>
                                <small><c:out value="${animal.especie}" /></small>
                            </td>
                            <td><c:out value="${animal.raza}" /></td>
                            <td><c:out value="${animal.pesoKg}" /> kg</td>
                            <td>$ <c:out value="${animal.precioCop}" /></td>
                            <td><span class="estado"><c:out value="${animal.estado}" /></span></td>
                            <td class="acciones">
                                <a class="boton boton--secundario" href="${pageContext.request.contextPath}/animales?accion=editar&id=${animal.id}">Editar</a>
                                <form method="post" action="${pageContext.request.contextPath}/animales" onsubmit="return confirm('¿Eliminar este animal?');">
                                    <input type="hidden" name="accion" value="eliminar">
                                    <input type="hidden" name="id" value="${animal.id}">
                                    <button class="boton boton--peligro" type="submit">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
