# API Alojamientos Ecoturísticos

Práctica final de la 1ª evaluación · Desarrollo Web en Entorno Servidor (DWES) · 2º DAW

API REST con Spring Boot 4 para gestionar alojamientos ecoturísticos (cabañas, glampings, casas rurales) y consultar el tiempo en su ubicación mediante la API pública **Open-Meteo**.

## Tecnologías

Java 25 · Spring Boot 4.1 · Spring Data JPA · H2 · Bean Validation · Lombok · springdoc-openapi (Swagger UI) · Caché en RAM · RestClient · JUnit 5 + Mockito

## Cómo arrancarlo

1. Abrir la carpeta en IntelliJ IDEA (File → Open).
2. Ejecutar `AlojamientosEcoturisticosApplication` (perfil `dev` por defecto).
3. Swagger UI: http://localhost:8080/swagger-ui.html
4. Consola H2 (solo dev): http://localhost:8080/h2-console · JDBC URL `jdbc:h2:mem:alojamientosdb` · usuario `sa`

## Perfiles

| Perfil | Base de datos | Consola H2 | ddl-auto |
|---|---|---|---|
| dev | H2 en memoria | Sí | update |
| test | H2 en memoria aislada | No | create-drop |
| prod | H2 en disco (`./data/`) | No | validate |

## Autores

- Diego A. Rielo Cabrera
- Jorge Casteleiro Monagas
