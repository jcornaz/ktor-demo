import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.myApp() {
  routing {
    get("/") {
      val name = call.parameters["name"] ?: "world"
      call.respondText { "Hello $name!" }
    }

    get("/greet/{name}") {
      val name = call.parameters["name"]
      call.respondText { "Hello $name!" }
    }
  }
}

fun main() {
  embeddedServer(Netty, port = 8081, module = Application::myApp)
      .start(wait = true)
}
