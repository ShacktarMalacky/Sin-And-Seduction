# Sin & Seduction

Código Kotlin/Ktor extraído del archivo ZIP original, conservado en el repositorio.

## Estado real

El proyecto es un núcleo inicial, no un MMORPG completo: los datos del motor se almacenan en memoria; el esquema SQL todavía no está conectado. Los módulos de NPC, clanes y WebSockets no están integrados de extremo a extremo con el servidor. No hay autenticación ni cliente jugable integrado; el observatorio es una visualización interactiva del sistema solar, no un cliente MMORPG. Antes de un despliegue público se requieren pruebas, seguridad y persistencia real.

## Compilar y ejecutar

Requisitos: JDK 17 y Gradle 8.7.

```sh
gradle build
gradle :app:run
```

La API escucha en el puerto 8080. La compilación en GitHub Actions se realiza en cada push y pull request. Su resultado debe comprobarse antes de considerar funcional la aplicación.

## Observatorio 3D

Al arrancar el servidor, la página principal sirve la visualización 3D. La interfaz consulta `/api/world` cada cinco segundos y muestra el estado en memoria; requiere conexión a Internet para cargar Three.js. Las distancias y velocidades de las órbitas son escalas visuales, no datos astronómicos precisos.
