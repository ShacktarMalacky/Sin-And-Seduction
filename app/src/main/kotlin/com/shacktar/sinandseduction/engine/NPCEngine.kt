package com.shacktar.sinandseduction.engine

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * SIN & SEDUCTION - THE HARCORE NPC & AI ENGINE
 * 
 * Este módulo gestiona NPCs que no son solo relleno; 
 * tienen agendas propias, seducen proactivamente y traicionan.
 */

class NPCEngine(val engine: UltimateEngine) {
    
    val npcs = ConcurrentHashMap<UUID, NonPlayerCharacter>()

    enum class Personality { PREDATOR, DECEIVER, SUCCUBUS, AVENGER }

    data class NonPlayerCharacter(
        val id: UUID,
        val name: String,
        val personality: Personality,
        var targetPlayerId: UUID? = null,
        var aggressive: Boolean = true
    )

    fun spawnDarkNPC(name: String, personality: Personality) {
        val id = UUID.randomUUID()
        val playerData = UltimateEngine.Player(
            id = id,
            name = name,
            power = 25.0,
            corruption = 40.0,
            status = SinStatus.LUSTFUL
        )
        engine.players[id] = playerData
        npcs[id] = NonPlayerCharacter(id, name, personality)
    }

    // Lógica de decisión de la IA del NPC
    fun processNPCTurns() {
        npcs.values.forEach { npc ->
            val stats = engine.players[npc.id] ?: return@forEach
            
            // Buscar una víctima (el jugador con menos voluntad cerca)
            val victim = engine.players.values
                .filter { it.id != npc.id && it.status != SinStatus.BROKEN }
                .minByOrNull { it.willpower }

            victim?.let { v ->
                when (npc.personality) {
                    Personality.SUCCUBUS -> {
                        // Las Succubus prefieren seducir y romper la voluntad
                        engine.worldEvents.add(engine.seduceAndCorrupt(npc.id, v.id))
                    }
                    Personality.PREDATOR -> {
                        // Los Predadores van directo a la traición si pueden
                        if (v.willpower < 40) {
                            engine.worldEvents.add(engine.betrayalVow(npc.id, v.id))
                        } else {
                            engine.seduceAndCorrupt(npc.id, v.id)
                        }
                    }
                    else -> { /* Otros comportamientos */ }
                }
            }
        }
    }
}
