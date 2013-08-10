import app.FoaasModule
import app.FuckOff
import app.FuckOffService

import static io.netty.buffer.Unpooled.copiedBuffer
import static org.ratpackframework.groovy.RatpackScript.ratpack
import static org.ratpackframework.groovy.Util.exec

ratpack {
  modules {
    register new FoaasModule(getClass().getResource("messages.properties"))
  }

  handlers { FuckOffService service ->
    prefix("img") {
      assets "public/img", [] as String[]
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
    get(":type/:p1/:p2?") {
      def (to, from, type) = betterPathTokens
      "send object if available" service.get(type, from, to)
    }

    assets "public", "index.html"
  }
}

