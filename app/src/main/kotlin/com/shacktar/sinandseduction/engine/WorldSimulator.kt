package com.shacktar.sinandseduction.engine

/** Small deterministic smoke simulation for local development. */
fun main() {
    val engine = SinEngine()
    val player = engine.spawnPlayer("Shacktar")
    val rival = engine.spawnPlayer("Rival_01")
    val seductress = engine.spawnPlayer("Seductress_X")

    println("--- INICIANDO SIMULACIÓN DE MUNDO ---")
    repeat(10) { turn ->
        val result = when (turn % 3) {
            0 -> engine.executeAttack(player, rival)
            1 -> engine.executeAttack(rival, seductress)
            else -> {
                engine.players[player]?.desire = 50
                engine.executeSeduction(player, seductress)
            }
        }
        println(result)
    }

    println("\n--- ESTADO FINAL DEL MUNDO ---")
    engine.players.values.forEach {
        println("${it.name} [${it.path}] -> Credits: ${it.credits.toInt()} | Infamy: ${it.infamy} | HP: ${it.hp.toInt()}")
    }
}
