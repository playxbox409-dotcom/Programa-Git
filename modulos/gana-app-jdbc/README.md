# GANA APP - Módulo JDBC de animales

Proyecto Java que implementa el CRUD del inventario ganadero mediante JDBC. El diseño sigue una arquitectura por capas y usa sentencias preparadas para proteger los parámetros de las consultas.

## Funcionalidades

- Insertar animales.
- Consultar un animal por identificador.
- Listar el inventario completo.
- Actualizar los datos de un animal.
- Eliminar un animal.
- Validar campos obligatorios y valores monetarios.
- Ejecutar cada operación dentro de una transacción JDBC.

## Tecnologías

- Java 21 o superior.
- JDBC 4.2.
- MySQL 8 o superior para producción.
- H2 en modo MySQL para pruebas de integración.
- Maven para compilación y pruebas.

## Configuración de MySQL

1. Ejecute `src/main/resources/db/schema-mysql.sql` en MySQL.
2. Copie `.env.example` como `.env` o defina las variables `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. Compile con `mvn clean test`.
4. Ejecute la aplicación con `mvn exec:java`.

La aplicación permite elegir las opciones de crear, consultar, actualizar y eliminar desde un menú de consola.

## Pruebas

Las pruebas usan una base H2 en memoria y verifican el ciclo completo:

```text
crear -> consultar -> actualizar -> listar -> eliminar
```

Ejecute:

```bash
mvn clean test
```

## Estándares aplicados

- Paquetes en minúsculas: `co.edu.sena.ganaapp`.
- Clases en PascalCase: `JdbcAnimalRepositorio`.
- Variables y métodos en camelCase: `buscarPorId`.
- Constantes en MAYÚSCULAS_CON_GUION_BAJO.
- Recursos JDBC cerrados con `try-with-resources`.
- Consultas SQL parametrizadas con `PreparedStatement`.
