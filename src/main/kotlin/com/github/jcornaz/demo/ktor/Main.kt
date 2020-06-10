package com.github.jcornaz.demo.ktor

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.Serializable

private class MissingRequiredParameter(val parameter: String) : Throwable()

@Serializable
data class GreetingResponse(
    val name: String
)

@Serializable
data class MyData(
    val name: String,
    val age: Int
)

fun main() {
    val server = embeddedServer(Netty, port = 8080) {

        install(DefaultHeaders)
        install(AutoHeadResponse)
        install(CallLogging)
        install(StatusPages) {
            status(HttpStatusCode.NotFound) {
                call.respondText(status = it) { "Got lost?" }
            }

            exception<MissingRequiredParameter> { exception ->
                call.respondText(status = HttpStatusCode.BadRequest) { "The parameter '${exception.parameter}' is mandatory" }
            }
        }

        install(ContentNegotiation) {
            json()
        }

        routing {
            get("/greet") {
                val name: String = call.parameters["name"] ?: throw MissingRequiredParameter("name")

                call.respond(GreetingResponse(name))
            }

            post("/greet") {
                val data: MyData = call.receive<MyData>()

                call.respondText(status = HttpStatusCode.Created) {
                    "Hi ${data.name}, you are ${data.age}!"
                }
            }
        }
    }

    server.start(wait = true)
}
