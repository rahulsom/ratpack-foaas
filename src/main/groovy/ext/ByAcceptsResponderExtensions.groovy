package ext

import org.ratpackframework.handling.ByAcceptsResponder
import org.ratpackframework.handling.Context

/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ByAcceptsResponderExtensions {
    static void pdf(ByAcceptsResponder source, Closure runnable) {
        source.type "application/pdf", runnable
    }
}
