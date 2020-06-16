package com.github.jcornaz.demo.ktor

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
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
import kotlinx.html.*
import kotlinx.serialization.Serializable

@Serializable
data class Person(
  val firstName: String,
  val lastName: String,
  val age: Int
)

@Serializable
data class User(val name: String, val isAdmin: Boolean) : Principal

fun Application.myApp() {
  install(DefaultHeaders)

  install(ContentNegotiation) {
    json()
  }

  install(Authentication) {
    basic {
      validate { (username, password) ->
        if (username == "john.doe" && password == "12345") {
          User(username, true)
        } else {
          null
        }
      }
    }
  }

  routing {
    get("/ui") {
      call.respondHtml {
        head {
          title { +"Ktor demo" }
        }
        body {
          h1 { +"Ktor is cool because:" }
          ul {
            li { +"Idiomatic, simple and safe Kotlin DSL" }
            li { +"Asynchronous with coroutines" }
          }
        }
      }
    }

    get("/") {
      val name = call.parameters["name"] ?: "world"
      call.respondText { "Hello $name!" }
    }

    get("/greet/{name}") {
      val name = call.parameters["name"]
      call.respondText { "Hello $name!" }
    }

    post("/person") {
      val person: Person = call.receive()
      call.respond(HttpStatusCode.Created, person)
    }

    authenticate(optional = false) {
      get("/users/me") {
        val user: User = call.principal() ?: error("Not authenticated")
        call.respond(user)
      }
    }
  }
}

fun main() {
  embeddedServer(Netty, port = 8080, module = Application::myApp)
    .start(wait = true)
}
