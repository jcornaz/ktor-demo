package com.github.jcornaz.demo.ktor

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MyAppTest {

    @Test
    fun testGreet() = withTestApplication(Application::myApp) {
        val call = handleRequest(HttpMethod.Get, "/greet/test")

        assert(call.requestHandled)

        with(call.response) {
            assertEquals(HttpStatusCode.OK, status())
            assertEquals("Hello test!", content)
        }
    }
}
