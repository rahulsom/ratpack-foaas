import app.FoaasModule
import app.FuckOff
import app.FuckOffService

import static io.netty.buffer.Unpooled.copiedBuffer
import static org.ratpackframework.groovy.RatpackScript.ratpack
import static org.ratpackframework.groovy.Util.exec
import static groovy.json.JsonOutput.toJson

ratpack {
  modules {
    register new FoaasModule(getClass().getResource("messages.properties"))
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
      response.send toJson(service.getApi())
    }

    prefix("mp3") {
      get(":type/:p1/:p2?") {
        def (to, from, type) = betterPathTokens
        FuckOff f = service.get(type, from, to)
        exec blocking, { f.toMp3() }, { byte[] bytes ->
            response.send "audio/mpeg", copiedBuffer(bytes)
        }
      }
    }

    prefix("stream") {
      assets "public", "stream.html"
    }

    get(":type/:p1/:p2?") {
      def (to, from, type) = betterPathTokens
      "send object if available" service.get(type, from, to)
    }

    assets "public", "index.html"
  }
}