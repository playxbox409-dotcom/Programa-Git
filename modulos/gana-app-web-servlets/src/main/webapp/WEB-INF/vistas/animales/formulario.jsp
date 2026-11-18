<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty animal.id ? 'Registrar' : 'Editar'} animal | GANA APP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<%@ include file="../comun/cabecera.jspf" %>
<main class="contenedor contenedor--estrecho">
    <a class="volver" href="${pageContext.request.contextPath}/animales">← Volver al inventario</a>
    <section class="formulario-tarjeta">
        <p class="etiqueta">Módulo de animales</p>
        <h1>${empty animal.id ? 'Registrar animal' : 'Editar animal'}</h1>
        <p>Los campos marcados con asterisco son obligatorios.</p>

        <c:if test="${not empty error}">
            <div class="alerta alerta--error" role="alert"><c:out value="${error}" /></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/animales" class="formulario">
            <input type="hidden" name="accion" value="${empty animal.id ? 'crear' : 'actualizar'}">
            <c:if test="${not empty animal.id}">
                <input type="hidden" name="id" value="${animal.id}">
            </c:if>

            <label>Propietario *
                <input name="propietarioId" type="number" min="1" required value="${empty animal.propietarioId ? '1' : animal.propietarioId}">
            </label>
            <label>Código *
                <input name="codigo" maxlength="30" required value="<c:out value='${animal.codigo}'/>">
            </label>
            <label>Nombre *
                <input name="nombre" maxlength="100" required value="<c:out value='${animal.nombre}'/>">
            </label>
            <label>Especie *
                <select name="especie" required>
                    <option value="BOVINO" ${animal.especie == 'BOVINO' ? 'selected' : ''}>Bovino</option>
                    <option value="PORCINO" ${animal.especie == 'PORCINO' ? 'selected' : ''}>Porcino</option>
                    <option value="EQUINO" ${animal.especie == 'EQUINO' ? 'selected' : ''}>Equino</option>
                    <option value="OVINO" ${animal.especie == 'OVINO' ? 'selected' : ''}>Ovino</option>
                    <option value="CAPRINO" ${animal.especie == 'CAPRINO' ? 'selected' : ''}>Caprino</option>
                </select>
            </label>
            <label>Raza *
                <input name="raza" maxlength="80" required value="<c:out value='${animal.raza}'/>">
            </label>
            <label>Fecha de nacimiento
                <input name="fechaNacimiento" type="date" value="${animal.fechaNacimiento}">
            </label>
            <label>Peso en kg *
                <input name="pesoKg" type="number" min="0.01" step="0.01" required value="${animal.pesoKg}">
            </label>
            <label>Precio en pesos colombianos *
                <input name="precioCop" type="number" min="0" step="1" required value="${animal.precioCop}">
            </label>
            <label>Ubicación *
                <input name="ubicacion" maxlength="120" required value="<c:out value='${animal.ubicacion}'/>">
            </label>
            <label>Estado *
                <select name="estado" required>
                    <option value="DISPONIBLE" ${animal.estado == 'DISPONIBLE' ? 'selected' : ''}>Disponible</option>
                    <option value="RESERVADO" ${animal.estado == 'RESERVADO' ? 'selected' : ''}>Reservado</option>
                    <option value="VENDIDO" ${animal.estado == 'VENDIDO' ? 'selected' : ''}>Vendido</option>
                    <option value="INACTIVO" ${animal.estado == 'INACTIVO' ? 'selected' : ''}>Inactivo</option>
                </select>
            </label>

            <div class="formulario__acciones">
                <a class="boton boton--secundario" href="${pageContext.request.contextPath}/animales">Cancelar</a>
                <button class="boton boton--primario" type="submit">
                    ${empty animal.id ? 'Registrar animal' : 'Guardar cambios'}
                </button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
