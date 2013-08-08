package ext

import app.FuckOff
import io.netty.buffer.Unpooled
import org.ratpackframework.groovy.Template
import org.ratpackframework.handling.ByAcceptsResponder
import org.ratpackframework.handling.Context

import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Template.groovyTemplate
import static org.ratpackframework.groovy.Util.with
/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ContextExtensions {

    static void "send boring"(Context context, FuckOff f) {
        context.response.send "plain/text", "$f.message $f.subtitle"
    }

    static void "send json"(Context context, FuckOff f) {
        context.response.send "application/json", toJson(f)
    }

    static void "send html"(Context context, FuckOff f) {
        context.render groovyTemplate("fuckoff.html", f: f)
    }

    static void "send pdf"(Context context, FuckOff f) {
        context.response.send "application/pdf", Unpooled.copiedBuffer(f.toPdf())
    }

    static void given(Context context, @DelegatesTo(ByAcceptsResponder) Closure clos) {
        with(context.accepts, clos)
    }
}
