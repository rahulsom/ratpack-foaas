package ext

import app.DestructurablePathTokens
import app.FuckOff
import io.netty.buffer.Unpooled
import ratpack.groovy.handling.GroovyContext
import ratpack.handling.Context

import static app.FoaasWebSocketBroadcaster._ as BROADCASTER
import static groovy.json.JsonOutput.toJson
import static ratpack.groovy.Groovy.groovyTemplate

/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ContextExtensions {

  static DestructurablePathTokens getBetterPathTokens(Context context) {
    new DestructurablePathTokens(context.pathTokens)
  }

  static void "send boring"(GroovyContext context, FuckOff f) {
    context.response.send "$f.message $f.subtitle"
  }

  static void "send json"(GroovyContext context, FuckOff f) {
    context.response.send toJson(f)
  }

  static void "send html"(GroovyContext context, FuckOff f) {
    context.render groovyTemplate("fuckoff.html", f: f)
  }

  static void "broadcast fuck"(GroovyContext context, FuckOff f) {
    BROADCASTER.broadcast toJson(f)
  }

  static void "send xml"(GroovyContext context, FuckOff f) {
    context.response.send f.toXml()
  }

  static void "send pdf"(GroovyContext context, FuckOff f) {
    context.response.send "application/pdf", Unpooled.copiedBuffer(f.toPdf())
  }

  static void "send object if available"(GroovyContext context, FuckOff f) {
    if (!f) {
      context.clientError 404
    } else {
      context.with {
        "broadcast fuck" f
        byContent {
          /* Remember, order matters */
          plainText { "send boring" f }
          json { "send json" f }
          xml { "send xml" f }
          html { "send html" f }
          type("application/pdf") { "send pdf" f }
        }

      }
    }
  }

}
