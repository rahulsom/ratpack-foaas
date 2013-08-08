package app

import org.ratpackframework.handling.Context
import org.ratpackframework.render.ByTypeRenderer

import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate

class FuckOffRenderer extends ByTypeRenderer<FuckOff> {

    public FuckOffRenderer() {
        super(FuckOff)
    }

    @Override
    void render(Context ctx, FuckOff f) {
        ctx.given {
            text { "send boring" f }
            html { "send html" f }
            json { "send json" f }
            pdf  { "send pdf" f }
        }
    }
}