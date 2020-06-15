import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
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

@Serializable
data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int
)

fun Application.myApp() {
  install(DefaultHeaders)

  install(ContentNegotiation) {
    json()
  }

  routing {
    get("/") {
      val name = call.parameters["name"] ?: "world"
      call.respondText { "Hello $name!" }
    }

    get("/greet/{name}") {
      val name = call.parameters["name"]
      call.respondText { "Hello $name!" }
    }

    post("people") {
      val person: Person = call.receive()
      call.respond(HttpStatusCode.Created, person)
    }
  }
}

fun main() {
  embeddedServer(Netty, port = 8081, module = Application::myApp)
      .start(wait = true)
}
