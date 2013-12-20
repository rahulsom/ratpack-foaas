import app.FoaasBroadcaster
import app.FoaasModule
import app.FuckOff
import app.FuckOffService
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json
import static ratpack.websocket.WebSockets.websocket

ratpack {
  modules {
    register new FoaasModule(getClass().getResource("messages.properties"))
    register new JacksonModule()
  }

  handlers { FuckOffService service ->
    assets "public", "index.html"

    handler {
      if (request.path.empty || request.path == "index.html") {
        response.headers.set "X-UA-Compatible", "IE=edge,chrome=1"
      }
      next()
    }

    get("api") {
      render json(service.getApi())
    }

    get("stream") {
      render file("public/stream.html")
    }

    get("ws") { FoaasBroadcaster broadcaster ->
      websocket(context) { ws ->
        broadcaster.register {
          ws.send(it)
        }
      } onClose {
        it.openResult.close()
      } connect()
    }

    get(":type/:p1/:p2?") {
      def (String to, String from, String type) = betterPathTokens
      def f = service.get(type, from, to)
      f ? render(f) : clientError(404)
    }

    prefix("mp3") {
      get(":type/:p1/:p2?") {
        def (String to, String from, String type) = betterPathTokens
        FuckOff f = service.get(type, from, to)
        background {
          f.toMp3()
        } then { byte[] bytes ->
          response.send "audio/mpeg", bytes
        }
      }
    }

  }
}

