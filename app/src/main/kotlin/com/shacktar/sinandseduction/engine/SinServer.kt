package com.shacktar.sinandseduction.engine

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.http.content.*
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ActionRequest(val attackerId: String, val targetId: String, val actionType: String)

@Serializable
data class PlayerSnapshot(
    val id: String,
    val name: String,
    val infamy: Int,
    val desire: Int,
    val hp: Double,
    val credits: Double,
    val path: String
)

@Serializable
data class WorldSnapshot(val players: List<PlayerSnapshot>, val playerCount: Int)

private fun SinEngine.snapshot(): List<PlayerSnapshot> = players.values
    .sortedBy { it.name.lowercase() }
    .map { player ->
        PlayerSnapshot(
            id = player.id.toString(),
            name = player.name,
            infamy = player.infamy,
            desire = player.desire,
            hp = player.hp,
            credits = player.credits,
            path = player.path.name
        )
    }

fun main() {
    val engine = SinEngine()
    val bossId = engine.spawnPlayer("Lord_Vile")
    engine.activeBounties[bossId] = 5000.0

    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, host = "0.0.0.0", port = port) {
        module(engine)
    }.start(wait = true)
}

fun Application.module(engine: SinEngine) {
    install(ContentNegotiation) { json() }

    routing {
        get("/api/world") {
            val snapshot = engine.snapshot()
            call.respond(WorldSnapshot(players = snapshot, playerCount = snapshot.size))
        }

        // Kept for existing API clients; response uses stable, JSON-safe DTOs.
        get("/status") {
            call.respond(engine.snapshot())
        }

        post("/action") {
            val request = runCatching { call.receive<ActionRequest>() }.getOrElse {
                call.respond(HttpStatusCode.BadRequest, mapOf("status" to "error", "message" to "Cuerpo JSON inválido"))
                return@post
            }
            val attackerId = runCatching { UUID.fromString(request.attackerId) }.getOrNull()
            val targetId = runCatching { UUID.fromString(request.targetId) }.getOrNull()
            if (attackerId == null || targetId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("status" to "error", "message" to "Identificadores UUID inválidos"))
                return@post
            }

            val result = when (request.actionType.uppercase()) {
                "ATTACK" -> engine.executeAttack(attackerId, targetId)
                "SEDUCE" -> engine.executeSeduction(attackerId, targetId)
                else -> {
                    call.respond(HttpStatusCode.BadRequest, mapOf("status" to "error", "message" to "Acción no válida"))
                    return@post
                }
            }
            val succeeded = !result.startsWith("ERROR")
            call.respond(
                if (succeeded) HttpStatusCode.OK else HttpStatusCode.NotFound,
                mapOf("status" to if (succeeded) "success" else "error", "message" to result)
            )
        }

        // Serves the actual observatory UI from the same Ktor origin as the API.
        staticResources("/", "static") {
            default("index.html")
        }
    }
}
