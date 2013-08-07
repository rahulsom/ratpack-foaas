package ext

import org.ratpackframework.handling.ByAcceptsResponder
import org.ratpackframework.handling.Context

/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ByAcceptsResponderExtensions {

    static void text(ByAcceptsResponder source, @DelegatesTo(Context) Closure runnable, Closure clos = wrapRunner(runnable)) {
            source.type "text/plain", clos
    }

    static void html(ByAcceptsResponder source, @DelegatesTo(Context) Closure runnable, Closure clos = wrapRunner(runnable)) {
            source.type "text/html", clos
    }

    static void json(ByAcceptsResponder source, @DelegatesTo(Context) Closure runnable, Closure clos = wrapRunner(runnable)) {
            source.type "application/json", clos
    }

    static void pdf(ByAcceptsResponder source, @DelegatesTo(Context) Closure runnable, Closure clos = wrapRunner(runnable)) {
        source.type "application/pdf", clos
    }

    private static Closure wrapRunner(Closure clos) {
        clos.owner.with { it.context }.with {
            clos.delegate = it
            clos
        }
    }
}
