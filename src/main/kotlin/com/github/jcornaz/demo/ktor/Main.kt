package com.github.jcornaz.demo.ktor

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
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

private class MissingRequiredParameter(val parameter: String) : Exception()
private class Unauthorized : Exception()

@Serializable
data class GreetingResponse(
    val name: String
)

@Serializable
data class MyData(
    val name: String,
    val age: Int
)

data class MyUser(val name: String) : Principal

fun main() {
  val server = embeddedServer(Netty, port = 8080) {
    myApp()
  }

  server.start(wait = true)
}

fun Application.myApp() {
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

    exception<Unauthorized> {
      call.respondText(status = HttpStatusCode.Unauthorized) { "This endpoints require valid authentication" }
    }
  }

  install(ContentNegotiation) {
    json()
  }

  install(Authentication) {
    basic {
      validate { (name, password) ->
        if (name == "JUGS" && password == "ktor") {
          MyUser(name)
        } else {
          null
        }
      }
    }
  }

  routing {
    authenticate(optional = false) {
      get("/greet") {
        val user: MyUser = call.principal() ?: throw Unauthorized()

        val name: String = call.parameters["name"] ?: throw MissingRequiredParameter("name")

        call.respond(GreetingResponse(name))
      }

      post("/greet") {
        val data: MyData = call.receive()

        call.respondText(status = HttpStatusCode.Created) {
          "Hi ${data.name}, you are ${data.age}!"
        }
      }
    }
  }
}
