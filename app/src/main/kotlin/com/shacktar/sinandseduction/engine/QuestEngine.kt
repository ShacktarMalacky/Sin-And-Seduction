package com.shacktar.sinandseduction.engine

import java.util.*

/**
 * SIN & SEDUCTION - THE CORRUPTION QUEST SYSTEM
 * Misiones dinámicas que se generan basadas en el pecado del jugador.
 */

class QuestEngine(val engine: UltimateEngine) {
    
    data class Quest(
        val id: UUID = UUID.randomUUID(),
        val title: String,
        val targetId: UUID,
        val rewardPower: Double,
        val requiredSin: Int
    )

    val activeQuests = mutableListOf<Quest>()

    fun generateCorruptionQuest(playerId: UUID) {
        val p = engine.players[playerId] ?: return
        
        // Si el jugador es muy pecador, le damos misiones de "Asesinato de Voluntad"
        if (p.sinLevel > 100) {
            val victim = engine.players.values.filter { it.id != playerId }.randomOrNull()
            victim?.let {
                val q = Quest(
                    title = "EL QUEBRANTO DE ${it.name}",
                    targetId = it.id,
                    rewardPower = 50.0,
                    requiredSin = 100
                )
                activeQuests.add(q)
                engine.worldEvents.add("📜 NUEVA MISIÓN OSCURA: ${p.name} debe quebrar a ${it.name}.")
            }
        }
    }
}
