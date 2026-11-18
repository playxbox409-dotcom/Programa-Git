# Arquitectura del módulo

```text
AplicacionConsola
       |
       v
 AnimalServicio
       |
       v
AnimalRepositorio <--- JdbcAnimalRepositorio
                            |
                            v
                   FabricaConexiones
                            |
                            v
                       MySQL / H2
```

## Responsabilidades

- `dominio`: representa las entidades y estados del negocio.
- `servicio`: aplica las validaciones y coordina los casos de uso.
- `repositorio`: define el contrato CRUD y su implementación JDBC.
- `configuracion`: administra la URL, el usuario, la clave y las conexiones.
- `aplicacion`: presenta el menú de consola y recibe datos del usuario.

La interfaz `AnimalRepositorio` desacopla el negocio de JDBC. La clase `JdbcAnimalRepositorio` implementa todas las consultas con `PreparedStatement`, controla transacciones y convierte las filas SQL en objetos `Animal`.
