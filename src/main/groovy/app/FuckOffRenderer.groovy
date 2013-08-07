package app

import groovy.transform.InheritConstructors
import org.ratpackframework.handling.Context
import org.ratpackframework.render.ByTypeRenderer

import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate

@InheritConstructors
class FuckOffRenderer extends ByTypeRenderer<FuckOff> {
    @Override
    void render(Context ctx, FuckOff f) {
        ctx.given {
            text { "send boring" "$f.message $f.subtitle" }
            html { "send html" groovyTemplate("fuckoff.html", f: f) }
            json { "send json" toJson(f) }
            pdf  { "send pdf" f }
        }
    }
}