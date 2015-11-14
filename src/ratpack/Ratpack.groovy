import app.FoaasBroadcaster
import app.FoaasModule
import app.FuckOff
import app.FuckOffService
import ratpack.exec.Blocking
import ratpack.groovy.template.TextTemplateModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {
  bindings {
    module(TextTemplateModule)
    module new FoaasModule(getClass().getResource("messages.properties"))
  }

  handlers { FuckOffService service ->

    files { it.dir("public").indexFiles("index.html") }

    all {
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

    path("ws") { FoaasBroadcaster broadcaster ->
      context.websocket { ws ->
        broadcaster.register {
          ws.send(it)
        }
      } connect {
        it.onClose {
          it.openResult.close()
        }
      }
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
        Blocking.get {
          f.toMp3()
        } then { byte[] bytes ->
          response.send "audio/mpeg", bytes
        }
      }
    }

  }
}

