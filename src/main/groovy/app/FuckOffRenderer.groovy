package app

import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.render.GroovyRendererSupport

import javax.inject.Inject

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.markupBuilder

class FuckOffRenderer extends GroovyRendererSupport<FuckOff> {

  private final ObjectMapper objectMapper;
  private final FoaasBroadcaster broadcaster;

  @Inject
  FuckOffRenderer(ObjectMapper objectMapper, FoaasBroadcaster broadcaster) {
    this.objectMapper = objectMapper
    this.broadcaster = broadcaster
  }

  @Override
  void render(GroovyContext context, FuckOff f) throws Exception {

    def jsonString = objectMapper.writeValueAsString(f)
    broadcaster.broadcast(jsonString)

    context.byContent {
      plainText {
        context.response.send "text/plain;charset=UTF-8", "$f.message $f.subtitle"
      }
      json {
        context.response.send "application/json", jsonString
      }
      xml {
        context.render markupBuilder("application/xml", "UTF-8") {
          fuckoff {
            message f.message
            subtitle f.subtitle
          }
        }
      }
      html {
        context.render groovyTemplate("fuckoff.html", f: f, "text/html;charset=UTF-8")
      }
      type("application/pdf") {
        context.response.send "application/pdf", f.toPdf()
      }
    }
  }

}
