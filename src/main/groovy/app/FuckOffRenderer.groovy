package app

import com.fasterxml.jackson.databind.ObjectWriter
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.render.GroovyRendererSupport
import ratpack.jackson.Jackson

import static app.FoaasWebSocketBroadcaster._ as BROADCASTER
import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.markupBuilder

class FuckOffRenderer extends GroovyRendererSupport<FuckOff> {

  @Override
  void render(GroovyContext context, FuckOff f) throws Exception {
    // Send out websocket message

    BROADCASTER.broadcast context.get(ObjectWriter).writeValueAsString(f)

    context.byContent {
      plainText {
        render "$f.message $f.subtitle"
      }
      json {
        render Jackson.json(f)
      }
      xml {
        render markupBuilder("application/xml", "UTF-8") {
          fuckoff {
            message f.message
            subtitle f.subtitle
          }
        }
      }
      html {
        render groovyTemplate("fuckoff.html", f: f)
      }
      type("application/pdf") {
        response.send "application/pdf", f.toPdf()
      }
    }
  }

}
