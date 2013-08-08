package app

import org.ratpackframework.handling.Context
import org.ratpackframework.render.ByTypeRenderer

import static org.ratpackframework.groovy.Util.with

class FuckOffRenderer extends ByTypeRenderer<FuckOff> {

    public FuckOffRenderer() {
        super(FuckOff)
    }

    @Override
    void render(Context ctx, FuckOff f) {
        with(ctx.accepts) {
            plainText {
              ctx."send boring" f
            }
            html {
              ctx."send html" f
            }
            json {
              ctx."send json" f
            }
            pdf  {
              ctx."send pdf" f
            }
        }
    }
}