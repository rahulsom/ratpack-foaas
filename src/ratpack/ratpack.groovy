import app.FoaasModule
import app.FuckOff
import app.FuckOffService

import static io.netty.buffer.Unpooled.copiedBuffer
import static org.ratpackframework.groovy.RatpackScript.ratpack

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
        response.send "audio/mpeg", copiedBuffer(f.toMp3())
      }
    }
    get(":type/:p1/:p2?") {
      def (to, from, type) = betterPathTokens
      "send object if available" service.get(type, from, to)
    }

    assets "public", "index.html"
  }
}

