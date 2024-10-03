# Postgres.java: Gestor de Conexión y Operaciones de Base de Datos PostgreSQL

## Resumen

Esta clase proporciona funcionalidades para establecer conexiones con una base de datos PostgreSQL, configurar el esquema inicial, y realizar operaciones básicas de inserción de usuarios y comentarios.

## Flujo del Proceso

```mermaid
graph TD
    A[Inicio] --> B[Establecer Conexión]
    B --> C{¿Conexión exitosa?}
    C -->|Sí| D[Configurar Base de Datos]
    C -->|No| E[Manejar Error]
    D --> F[Crear Tablas]
    F --> G[Insertar Datos de Prueba]
    G --> H[Insertar Usuarios]
    H --> I[Insertar Comentarios]
    I --> J[Cerrar Conexión]
    J --> K[Fin]
    E --> K
```

## Insights

- Utiliza variables de entorno para la configuración de la base de datos, mejorando la seguridad y flexibilidad.
- Implementa un método de hash MD5 para el almacenamiento de contraseñas, aunque MD5 no se considera seguro para este propósito en la actualidad.
- Incluye manejo de excepciones y cierre de recursos en bloques finally para mejorar la robustez.
- Utiliza PreparedStatements para las inserciones, lo que ayuda a prevenir inyecciones SQL.
- Genera UUIDs para los identificadores de usuarios y comentarios.

## Dependencias

```mermaid
graph LR
    Postgres.java --- |"Utiliza"| PostgreSQL["Base de Datos PostgreSQL"]
    Postgres.java --- |"Importa"| java.sql["java.sql"]
    Postgres.java --- |"Importa"| java.math["java.math"]
    Postgres.java --- |"Importa"| java.security["java.security"]
    Postgres.java --- |"Importa"| java.util["java.util"]
```

- `PostgreSQL`: Base de datos utilizada para almacenar usuarios y comentarios.
- `java.sql`: Proporciona clases para la conexión y manipulación de bases de datos.
- `java.math`: Utilizada para operaciones matemáticas de precisión arbitraria (BigInteger).
- `java.security`: Proporciona clases e interfaces para funciones de seguridad y criptografía.
- `java.util`: Utilizada para generar UUIDs.

## Manipulación de Datos (SQL)

| Entidad   | Atributos                                                   | Descripción                                |
|-----------|-------------------------------------------------------------|-------------------------------------------|
| users     | user_id, username, password, created_on, last_login         | Almacena información de usuarios           |
| comments  | id, username, body, created_on                              | Almacena comentarios de usuarios           |

- `users`: CREATE TABLE para crear la tabla de usuarios con campos para ID, nombre de usuario, contraseña y marcas de tiempo.
- `comments`: CREATE TABLE para crear la tabla de comentarios con campos para ID, nombre de usuario, contenido y marca de tiempo.
- `users`: DELETE para limpiar datos existentes antes de insertar nuevos registros.
- `comments`: DELETE para limpiar datos existentes antes de insertar nuevos registros.
- `users`: INSERT para agregar nuevos usuarios con contraseñas hasheadas.
- `comments`: INSERT para agregar nuevos comentarios asociados a usuarios.
