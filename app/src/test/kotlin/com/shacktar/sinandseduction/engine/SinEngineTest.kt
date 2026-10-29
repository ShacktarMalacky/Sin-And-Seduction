package com.shacktar.sinandseduction.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SinEngineTest {
    @Test fun attackChangesHealthAndInfamy() {
        val engine = SinEngine()
        val attacker = engine.spawnPlayer("Atacante")
        val target = engine.spawnPlayer("Objetivo")
        engine.executeAttack(attacker, target)
        assertEquals(90.0, engine.players.getValue(target).hp)
        assertEquals(5, engine.players.getValue(attacker).infamy)
    }
    @Test fun rejectsUnknownAttacker() {
        val engine = SinEngine()
        val target = engine.spawnPlayer("Objetivo")
        assertTrue(engine.executeAttack(java.util.UUID.randomUUID(), target).startsWith("ERROR"))
    }
}
