import app.FoaasModule
import app.FuckOff
import app.FuckOffService
import ratpack.jackson.JacksonModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {
  modules {
    register new FoaasModule(getClass().getResource("messages.properties"))
    register new JacksonModule()
  }

  handlers { FuckOffService service ->
    handler {
      if (request.path.empty || request.path == "index.html") {
        response.headers.set "X-UA-Compatible", "IE=edge,chrome=1"
      }
      next()
    }

    prefix("img") {
      assets "public/img", [] as String[]
    }

    get("api") {
      render json(service.getApi())
    }

    prefix("mp3") {
      get(":type/:p1/:p2?") {
        def (to, from, type) = betterPathTokens
        FuckOff f = service.get(type, from, to)
        background {
          f.toMp3()
        } then { byte[] bytes ->
          response.send "audio/mpeg", bytes
        }
      }
    }

    prefix("stream") {
      assets "public", "stream.html"
    }

    get(":type/:p1/:p2?") {
      def (to, from, type) = betterPathTokens
      def f = service.get(type, from, to)
      f ? render(f) : clientError(404)
    }

    assets "public", "index.html"
  }
}