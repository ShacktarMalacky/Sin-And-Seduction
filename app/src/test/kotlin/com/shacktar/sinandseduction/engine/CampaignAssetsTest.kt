package com.shacktar.sinandseduction.engine

import kotlin.test.Test
import kotlin.test.assertTrue
import java.io.File

class CampaignAssetsTest {
    @Test
    fun staticCampaignContainsThreeSceneAndGameLoop() {
        val js = File("src/main/resources/static/game.js").readText()
        assertTrue(js.contains("new THREE.Scene()"))
        assertTrue(js.contains("new THREE.PerspectiveCamera"))
        assertTrue(js.contains("new THREE.WebGLRenderer"))
        assertTrue(js.contains("requestAnimationFrame(animate)"))
        assertTrue(js.contains("clock.getDelta()"))
    }

    @Test
    fun campaignOperationsAndSaveArePresent() {
        val js = File("src/main/resources/static/game.js").readText()
        assertTrue(js.contains("function operation(action)"))
        assertTrue(js.contains("function nextCycle()"))
        assertTrue(js.contains("function capture(p,w)"))
        assertTrue(js.contains("localStorage.setItem(SAVE_KEY"))
        assertTrue(js.contains("function repairShip()"))
        assertTrue(js.contains("function renderContracts()"))
        assertTrue(js.contains("function updateContracts()"))
        assertTrue(js.contains("contractClaims"))
        assertTrue(js.contains("sin-seduction-campaign-v3"))
        val html = File("src/main/resources/static/index.html").readText()
        assertTrue(html.contains("Eclipse Nocturna · Conquista Solar"))
        assertTrue(html.contains("CONTRATOS DE CAMPAÑA"))
    }

    @Test
    fun everyReferencedUiElementExists() {
        val base = File("src/main/resources/static")
        val html = File(base, "index.html").readText()
        val js = File(base, "game.js").readText()
        val ids = Regex("id=\"([^\"]+)\"").findAll(html).map { it.groupValues[1] }.toSet()
        val refs = Regex("\\$\\('([^']+)'\\)").findAll(js).map { it.groupValues[1] }.toSet()
        assertTrue(refs.all { it in ids }, "Missing HTML elements: ${refs - ids}")
    }
}
