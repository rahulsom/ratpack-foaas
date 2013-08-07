package ext

import app.FuckOff
import io.netty.buffer.Unpooled
import org.ratpackframework.groovy.Template
import org.ratpackframework.handling.ByAcceptsResponder
import org.ratpackframework.handling.Context

import static org.ratpackframework.groovy.Util.with
/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ContextExtensions {

    static void "send boring"(Context context, String txt) {
        context.response.send "plain/text", txt
    }

    static void "send json"(Context context, String txt) {
        context.response.send "application/json", txt
    }

    static void "send html"(Context context, Template template) {
        context.render template
    }

    static void "send pdf"(Context context, FuckOff f) {
        context.response.send "application/pdf", Unpooled.copiedBuffer(f.toPdf())
    }

    static void given(Context context, @DelegatesTo(ByAcceptsResponder) Closure clos) {
        with(context.accepts, clos)
    }
}
