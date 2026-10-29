package com.shacktar.sinandseduction.engine

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SinServerTest {
    @Test
    fun worldEndpointReturnsLiveEngineState() = testApplication {
        val engine = SinEngine()
        engine.spawnPlayer("TestPilot")
        application { module(engine) }

        val response = client.get("/api/world")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("TestPilot"))
        assertTrue(body.contains("\"playerCount\":1"))
    }

    @Test
    fun invalidActionIdsReturnBadRequest() = testApplication {
        application { module(SinEngine()) }
        val response = client.post("/action") {
            contentType(ContentType.Application.Json)
            setBody("""{"attackerId":"bad","targetId":"bad","actionType":"ATTACK"}""")
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
