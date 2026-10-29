package com.shacktar.sinandseduction.engine

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * SIN & SEDUCTION - THE INDUSTRIAL ENGINE
 * 
 * Este es el núcleo real para producción. Maneja:
 * - Persistencia lógica de entidades.
 * - Sistema de Combate con multiplicadores por Path.
 * - Motor de Seducción y Vínculos (Bonds).
 * - Economía basada en Bounties (Recompensas).
 */

enum class Path { NEUTRAL, OVERLORD, TEMPTRESS }
enum class BondType { BLOOD_VOW, RIVALRY, SEDUCTION }

class SinEngine {
    
    // Almacenamiento persistente en memoria (Listo para mapear a SQL)
    val players = ConcurrentHashMap<UUID, Player>()
    val activeBounties = ConcurrentHashMap<UUID, Double>()
    val socialBonds = mutableListOf<Bond>()

    data class Player(
        val id: UUID,
        val name: String,
        var infamy: Int = 0,
        var desire: Int = 0,
        var hp: Double = 100.0,
        var credits: Double = 1000.0,
        var path: Path = Path.NEUTRAL
    )

    data class Bond(
        val a: UUID,
        val b: UUID,
        val type: BondType,
        var power: Int = 10
    )

    // --- SISTEMA DE COMBATE ---
    fun executeAttack(attackerId: UUID, targetId: UUID): String {
        val attacker = players[attackerId] ?: return "ERROR: Atacante no encontrado."
        val target = players[targetId] ?: return "ERROR: Objetivo no encontrado."

        // Multiplicador de daño por Path
        val pathBonus = if (attacker.path == Path.OVERLORD) 1.5 else 1.0
        val damage = (10 + (attacker.infamy * 0.05)) * pathBonus
        
        target.hp -= damage
        attacker.infamy += 5

        var result = "${attacker.name} infligió ${damage.toInt()} de daño a ${target.name}."

        if (target.hp <= 0) {
            val loot = (activeBounties.remove(targetId) ?: 0.0) + (target.credits * 0.2)
            attacker.credits += loot
            target.hp = 100.0 // Reset/Respawn
            result += " | ¡OBJETIVO ELIMINADO! Loot obtenido: $loot créditos."
            updatePath(attacker)
        }
        return result
    }

    // --- SISTEMA DE SEDUCCIÓN ---
    fun executeSeduction(sedId: UUID, targetId: UUID): String {
        val seducer = players[sedId] ?: return "ERROR"
        val target = players[targetId] ?: return "ERROR"

        if (seducer.desire < 50) return "Deseo insuficiente para seducir a ${target.name}."

        seducer.desire += 15
        target.hp -= 5 // La seducción debilita la voluntad (HP lógico)
        
        // Crear vínculo si no existe
        if (socialBonds.none { it.a == sedId && it.b == targetId }) {
            socialBonds.add(Bond(sedId, targetId, BondType.SEDUCTION))
        }

        updatePath(seducer)
        return "${seducer.name} ha envuelto a ${target.name} en sus redes de deseo."
    }

    private fun updatePath(p: Player) {
        if (p.path != Path.NEUTRAL) return
        
        if (p.infamy >= 1000) {
            p.path = Path.OVERLORD
        } else if (p.desire >= 1000) {
            p.path = Path.TEMPTRESS
        }
    }

    fun spawnPlayer(name: String): UUID {
        val id = UUID.randomUUID()
        players[id] = Player(id, name)
        return id
    }
}
