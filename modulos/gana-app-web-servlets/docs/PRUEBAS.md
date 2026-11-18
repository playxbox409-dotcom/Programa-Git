# Pruebas del módulo

Comando:

```bash
mvn clean test package
```

La prueba de integración usa JDBC con H2 en modo MySQL y recorre el CRUD completo de animales. El empaquetado genera `target/gana-app-web.war`, listo para desplegar en Tomcat 11.
