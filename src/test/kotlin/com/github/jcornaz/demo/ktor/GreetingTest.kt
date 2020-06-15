package com.github.jcornaz.demo.ktor

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GreetingTest {

    @Test
    @OptIn(ImplicitReflectionSerializer::class)
    fun canGetGreeting() = withTestApplication(Application::myApp) {
        val call = handleRequest(HttpMethod.Get, "/greet?name=test") {
            val credentials = "JUGS:ktor"
            addHeader("Authorization", "Basic ${Base64.getEncoder().encodeToString(credentials.toByteArray())}")
        }
        with(call.response) {
            assertEquals(HttpStatusCode.OK, status())

            val content = Json(JsonConfiguration.Stable).parse<GreetingResponse>(assertNotNull(content))
            assertEquals(content, GreetingResponse("test"))
        }
    }
}
