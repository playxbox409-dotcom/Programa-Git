# Resultado de pruebas

Fecha de ejecución: 18 de noviembre de 2026.

Comando ejecutado:

```bash
mvn clean test
```

Resultado:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

La prueba `JdbcAnimalRepositorioTest` crea una base H2 en memoria configurada en modo MySQL y valida las siguientes operaciones mediante JDBC:

1. Inserción de un animal y recuperación de la llave generada.
2. Consulta por identificador.
3. Consulta del listado completo.
4. Actualización del precio y del estado.
5. Eliminación y comprobación de inexistencia.
