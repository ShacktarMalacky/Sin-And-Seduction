package com.shacktar.sinandseduction.engine

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ActionRequest(val attackerId: String, val targetId: String, val actionType: String)

fun main() {
    val engine = SinEngine()
    
    // Setup inicial de prueba
    val bossId = engine.spawnPlayer("Lord_Vile")
    engine.activeBounties[bossId] = 5000.0

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        
        routing {
            get("/status") {
                call.respond(engine.players.values.toList())
            }
            
            post("/action") {
                val req = call.receive<ActionRequest>()
                val aId = UUID.fromString(req.attackerId)
                val tId = UUID.fromString(req.targetId)
                
                val result = when(req.actionType) {
                    "ATTACK" -> engine.executeAttack(aId, tId)
                    "SEDUCE" -> engine.executeSeduction(aId, tId)
                    else -> "Acción no válida"
                }
                call.respond(mapOf("status" to "success", "message" to result))
            }
        }
    }.start(wait = true)
}
