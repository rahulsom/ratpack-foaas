package ext

import app.DestructurablePathTokens
import app.FuckOff
import io.netty.buffer.Unpooled
import org.ratpackframework.handling.Context

import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate
/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ContextExtensions {

  static DestructurablePathTokens getBetterPathTokens(Context context) {
    new DestructurablePathTokens(context.pathTokens)
  }

  static void "send boring"(Context context, FuckOff f) {
    context.response.send "$f.message $f.subtitle"
  }

  static void "send json"(Context context, FuckOff f) {
    context.response.send toJson(f)
  }

  static void "send html"(Context context, FuckOff f) {
    context.render groovyTemplate("fuckoff.html", f: f)
  }

  static void "send xml"(Context context, FuckOff f) {
    context.response.send f.toXml()
  }

  static void "send pdf"(Context context, FuckOff f) {
    context.response.send "application/pdf", Unpooled.copiedBuffer(f.toPdf())
  }

  static void "send object if available"(Context context, FuckOff f) {
    if (!f) {
      context.clientError 404
    } else {
      context.with {
        respond byContent
        /* Remember, order matters */
          .plainText { "send boring" f }
          .json { "send json" f }
          .xml { "send xml" f }
          .html { "send html" f }
          .type("application/pdf") { "send pdf" f }
      }
    }
  }
}
