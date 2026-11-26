# GANA APP — Componente front-end

Módulo web responsive para administrar el inventario ganadero del proyecto formativo GANA APP.

## Funcionalidades

- Consultar animales registrados.
- Registrar animales con validaciones HTML y JavaScript.
- Editar los datos existentes.
- Eliminar registros con confirmación.
- Calcular estadísticas del inventario.
- Guardar los datos en `localStorage` para ejecutar el prototipo sin servidor.

## Ejecución

1. Extraer la carpeta de la evidencia.
2. Abrir `index.html` en Microsoft Edge, Google Chrome o Firefox.
3. Usar el botón **Registrar animal** para crear el primer registro.

También se puede servir la carpeta con una extensión como Live Server o con cualquier servidor HTTP local.

## Estructura

- `index.html`: inventario y resumen.
- `form.html`: formulario compartido para registro y edición.
- `css/styles.css`: sistema visual adaptable a móvil y escritorio.
- `js/storage.js`: persistencia y operaciones CRUD.
- `js/list.js`: renderizado del inventario y eliminación.
- `js/form.js`: validaciones, registro y edición.
- `assets/logo_gana_app.png`: identidad visual del proyecto.

## Tecnologías

HTML5 semántico, CSS3 responsive, JavaScript ES6, Web Storage API y Git.
