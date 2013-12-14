package app

import com.fasterxml.jackson.databind.ObjectWriter
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.render.GroovyRendererSupport
import ratpack.jackson.Jackson

import javax.inject.Inject

import static FoaasBroadcaster._ as BROADCASTER
import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.markupBuilder

class FuckOffRenderer extends GroovyRendererSupport<FuckOff> {

  private final ObjectWriter objectWriter;
  private final FoaasBroadcaster broadcaster;

  @Inject
  FuckOffRenderer(ObjectWriter objectWriter, FoaasBroadcaster broadcaster) {
    this.objectWriter = objectWriter
    this.broadcaster = broadcaster
  }

  @Override
  void render(GroovyContext context, FuckOff f) throws Exception {

    def jsonString = objectWriter.writeValueAsString(f)
    broadcaster.broadcast(jsonString)

    context.byContent {
      plainText {
        render "$f.message $f.subtitle"
      }
      json {
        response.send "application/json", jsonString
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
