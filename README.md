# API Alojamientos Ecoturísticos

Práctica final de la 1ª evaluación · Desarrollo Web en Entorno Servidor (DWES) · 2º DAW

API REST con Spring Boot 4 para gestionar alojamientos ecoturísticos (cabañas, glampings y casas rurales) y consultar la **previsión del tiempo de varios días** en su ubicación mediante la API pública **Open-Meteo**, recomendando qué actividades del alojamiento se pueden hacer según el tiempo.

## Tecnologías

Java 25 · Spring Boot 4.1 · Spring Data JPA · H2 · Bean Validation · Lombok · springdoc-openapi (Swagger UI) · Caché en RAM · RestClient · JUnit 5 + Mockito

## Cómo arrancarlo

1. Abrir la carpeta en IntelliJ IDEA (File → Open).
2. Ejecutar `AlojamientosEcoturisticosApplication` (perfil `dev` por defecto). Se cargan 5 alojamientos de ejemplo.
3. Swagger UI: http://localhost:8080/swagger-ui.html
4. Consola H2 (solo dev): http://localhost:8080/h2-console · JDBC URL `jdbc:h2:mem:alojamientosdb` · usuario `sa`

## Endpoints

Ruta base: `/api/alojamientos`

| Verbo | Ruta | Qué hace | Códigos |
|---|---|---|---|
| GET | `/` | Lista todos los alojamientos | 200 |
| GET | `/{id}` | Devuelve un alojamiento | 200, 404 |
| GET | `/buscar?provincia=Huesca` | Busca por provincia (sin distinguir mayúsculas) | 200 |
| GET | `/precio-maximo?max=80` | Alojamientos con precio por noche menor o igual | 200 |
| GET | `/certificados` | Alojamientos con certificado ecológico | 200 |
| POST | `/` | Crea un alojamiento | 201, 400, 409, 422 |
| PUT | `/{id}` | Sustituye un alojamiento completo | 200, 400, 404, 409, 422 |
| PATCH | `/{id}` | Modifica solo los campos enviados | 200, 400, 404, 422 |
| DELETE | `/{id}` | Elimina un alojamiento | 204, 404 |
| GET | `/{id}/prevision?dias=3` | Mashup: alojamiento + previsión de Open-Meteo + actividades recomendadas | 200, 404, 422 |
| GET | `/entorno` | Perfil activo | 200 |

## Errores

Todos los errores devuelven el mismo JSON (`status`, `mensaje`, `timestamp` y `erroresCampo`), gestionado por `ManejadorExcepcionesGlobal`:

| Código | Cuándo |
|---|---|
| 400 | Los datos no cumplen las validaciones (detalle por campo en `erroresCampo`) |
| 404 | El alojamiento no existe |
| 409 | El código de registro ya existe |
| 422 | Regla de negocio: capacidad máxima según el tipo (cabaña 8, glamping 4, casa rural 20) o días de previsión fuera de 1 a 7 |
| 500 | Error inesperado (sin detalles internos) |

## La previsión (mashup con Open-Meteo)

`GET /api/alojamientos/{id}/prevision?dias=3` usa las coordenadas del alojamiento para pedir a Open-Meteo la previsión diaria (temperatura máxima y mínima, probabilidad de lluvia e índice UV) y la devuelve día a día. Además, cruza las actividades del alojamiento con el tiempo: las de exterior solo se recomiendan si ningún día supera el 70 % de probabilidad de lluvia.

- **Caché:** la previsión de cada alojamiento y número de días se guarda en memoria; se vacía al crear, modificar o borrar un alojamiento.
- **Fallback:** si Open-Meteo no responde (sin conexión, timeout o límite diario), la API responde igualmente con `datosEnVivo: false`, nunca con un error 500.

## Perfiles

| Perfil | Base de datos | Consola H2 | ddl-auto | Datos de ejemplo |
|---|---|---|---|---|
| dev | H2 en memoria | Sí | update | Sí |
| test | H2 en memoria aislada | No | create-drop | No |
| prod | H2 en disco (`./data/`) | No | validate | No |

En **prod**, la primera vez hay que arrancar con `ddl-auto=update` para crear la base de datos en disco; después, `validate`.

## Tests

Ejecutar desde IntelliJ: clic derecho en `src/test/java` → **Run 'All Tests'**.

| Tipo | Clase | Qué prueba |
|---|---|---|
| Repositorio (`@DataJpaTest`) | `AlojamientoRepositoryTest` | Métodos derivados y la colección de actividades contra H2 |
| Unitario de servicio (Mockito) | `AlojamientoServiceUnitTest` | Lógica de negocio con el repositorio simulado |
| Unitario de controlador (`@WebMvcTest`) | `AlojamientoControllerUnitTest` | Endpoints y códigos HTTP con el servicio simulado |
| Integración (`@SpringBootTest`) | `AlojamientoControllerIntegrationTest` | La aplicación completa de principio a fin |

## Pruebas manuales

El fichero `pruebas.http` (raíz del proyecto) contiene todos los casos de éxito y de error, listos para lanzar desde IntelliJ con la aplicación arrancada.

## Autores

- Diego A. Rielo Cabrera
- Jorge Casteleiro Monagas
