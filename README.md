# 🌘 Eclipse Nocturna — Conquista Solar

Núcleo de servidor MMORPG oscuro, escrito en Kotlin sobre Ktor. El repositorio reúne combate, seducción, corrupción, traición, clanes de sangre, eventos globales, WebSockets y persistencia PostgreSQL.

## Estado

> Prototipo técnico de servidor: la base funcional está lista para evolucionar hacia producción. La persistencia aún debe conectarse a una capa de repositorios y autenticación antes de un despliegue público.

## Sistemas

- **Combate y progresión:** infamia, deseo, rutas `NEUTRAL`, `OVERLORD` y `TEMPTRESS`.
- **Corrupción y traición:** voluntad, vínculos, robo de poder y evolución a `GOD_MODE`.
- **Clanes de sangre:** creación, miembros, pureza, infamia y usurpación.
- **IA de NPCs:** personalidades predadoras, seductoras y de infiltración.
- **Tiempo real:** canal WebSocket `/world` para eventos del mundo.
- **API HTTP:** `GET /status` y `POST /action`.
- **Persistencia:** esquema PostgreSQL en `sql/SinAndSeduction_Persistence.sql`.

## Requisitos

- JDK 17+
- Gradle 8.7+ (el workflow de GitHub Actions lo instala automáticamente)
- PostgreSQL 14+ para la persistencia

## Ejecutar

```bash
gradle --no-daemon :app:run
```

El servidor escucha en el puerto `8080`. Para construirlo:

```bash
gradle --no-daemon build
```

Para ejecutar la simulación local del mundo:

```bash
gradle --no-daemon :app:run --args='--simulation'
```

## Docker

```bash
docker build -t sin-and-seduction .
docker run --rm -p 8080:8080 sin-and-seduction
```

## API rápida

`GET /status` devuelve los jugadores activos.

`POST /action` acepta:

```json
{"attackerId":"UUID", "targetId":"UUID", "actionType":"ATTACK"}
```

Acciones disponibles: `ATTACK` y `SEDUCE`.

## CI

Cada push y pull request hacia `main` o `master` ejecuta compilación y pruebas mediante GitHub Actions.

## Licencia

Proyecto de ShacktarMalacky. Define una licencia antes de aceptar contribuciones externas.

## Observatorio 3D integrado

El sistema solar se sirve desde `app/src/main/resources/static/index.html` en la ruta raíz (`/`) del mismo servidor que expone la API. La interfaz consulta `/api/world` cada cinco segundos y muestra el estado vivo del motor. Requiere Internet para descargar Three.js desde CDN. La ruta `/status` se mantiene para clientes anteriores.

## Eclipse Nocturna · Campaña Conquista Solar

Eclipse Nocturna es el título visible de esta experiencia web. La interfaz utiliza una escena Three.js con cámara orbital, planetas seleccionables y una nave cuya potencia se desarrolla mediante mejoras. La campaña incluye reconocimiento, infiltración, asalto, corrupción, resistencia planetaria, rutas desbloqueables, energía, integridad de casco, reparaciones, recargas de suministros, créditos, experiencia, mejoras de nave, contraataques, victoria, contratos secundarios con recompensas, hangar orbital (armas, blindaje y reactor mejorables con efectos reales en combate) y guardado local compatible por navegador. La campaña es de un solo jugador y el progreso vive en localStorage; no es multijugador persistente. El estado `/api/world` del servidor se muestra cuando Ktor está disponible, pero no sincroniza el guardado de campaña.

Controles: seleccionar planeta, reconocerlo, elegir operaciones, agotar energía y cerrar el ciclo; la velocidad, pausa, órbitas y progreso se guardan localmente.

La versión web actual es una campaña individual que se ejecuta completamente en el navegador: la economía, defensas, ciclos y progreso se guardan en `localStorage`. No sincroniza acciones ni guardado con PostgreSQL y aún no constituye un MMORPG multijugador. Usa Three.js por CDN, por lo que requiere conexión para descargar esa librería.
