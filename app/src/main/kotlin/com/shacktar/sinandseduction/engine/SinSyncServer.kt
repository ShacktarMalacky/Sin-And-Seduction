package com.shacktar.sinandseduction.engine

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * SIN & SEDUCTION - REAL-TIME SYNC ENGINE (WebSockets)
 * Este módulo permite que cada traición, seducción o evolución 
 * se transmita INSTANTÁNEAMENTE a todos los jugadores conectados.
 */

class SinSyncServer(val engine: UltimateEngine) {
    private val sessions = ConcurrentHashMap<UUID, DefaultWebSocketSession>()

    fun install(app: Application) {
        app.install(WebSockets)
        app.routing {
            webSocket("/world") {
                val clientId = UUID.randomUUID()
                sessions[clientId] = this
                try {
                    send("CONECTADO AL MUNDO DE SIN & SEDUCTION. TU ID: $clientId")
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            // Lógica de procesamiento de comandos en tiempo real aquí
                        }
                    }
                } finally {
                    sessions.remove(clientId)
                }
            }
        }
    }

    suspend fun broadcastEvent(message: String) {
        sessions.values.forEach { session ->
            session.send(Frame.Text("🚨 EVENTO GLOBAL: $message"))
        }
    }
}
