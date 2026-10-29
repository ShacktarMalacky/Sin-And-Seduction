package com.shacktar.sinandseduction.engine

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * SIN & SEDUCTION - THE ULTIMATE DARK ENGINE
 * Este no es un prototipo. Es el núcleo de mecánicas prohibidas.
 */

enum class Trait { CORRUPTED, DOMINANT, SUBMISSIVE, DECEITFUL }
enum class SinStatus { INFAMOUS, LUSTFUL, BROKEN, GOD_MODE }

class UltimateEngine {
    val players = ConcurrentHashMap<UUID, Player>()
    val worldEvents = mutableListOf<String>()

    data class Player(
        val id: UUID,
        val name: String,
        var sinLevel: Int = 0,
        var corruption: Double = 0.0,
        var willpower: Double = 100.0,
        var power: Double = 10.0,
        var status: SinStatus = SinStatus.LUSTFUL,
        val traits: MutableSet<Trait> = mutableSetOf()
    )

    // MECÁNICA: SEDUCCIÓN AGRESIVA (Romper la voluntad)
    fun seduceAndCorrupt(seducerId: UUID, victimId: UUID): String {
        val s = players[seducerId] ?: return "ERROR"
        val v = players[victimId] ?: return "ERROR"

        val seductionPower = s.power * (1 + s.corruption / 100)
        v.willpower -= seductionPower
        s.corruption += 2.0
        
        return if (v.willpower <= 0) {
            v.status = SinStatus.BROKEN
            v.traits.add(Trait.SUBMISSIVE)
            s.sinLevel += 50
            "🔥 ${v.name} ha sido quebrantado. Ahora es un títere de ${s.name}."
        } else {
            "${s.name} está erosionando la voluntad de ${v.name} ($seductionPower daño mental)."
        }
    }

    // MECÁNICA: TRAICIÓN DE SANGRE (Robo de poder)
    fun betrayalVow(traitorId: UUID, partnerId: UUID): String {
        val t = players[traitorId] ?: return "ERROR"
        val p = players[partnerId] ?: return "ERROR"

        if (p.status == SinStatus.BROKEN || p.willpower < 50) {
            val stolenPower = p.power * 0.3
            t.power += stolenPower
            p.power -= stolenPower
            t.traits.add(Trait.DECEITFUL)
            t.sinLevel += 100
            return "💀 ¡TRAICIÓN CHINGONA! ${t.name} absorbió el $stolenPower de poder de ${p.name}."
        }
        return "${p.name} todavía tiene mucha voluntad para ser traicionado así."
    }

    // MECÁNICA: EVOLUCIÓN A DIOS (Endgame)
    fun checkGodMode(playerId: UUID) {
        val p = players[playerId] ?: return
        if (p.sinLevel > 5000 && p.corruption > 80.0) {
            p.status = SinStatus.GOD_MODE
            worldEvents.add("🚨 EL MUNDO TIEMBLA: ${p.name} ha alcanzado el GOD MODE.")
        }
    }
}
