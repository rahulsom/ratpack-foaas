package ext

import app.DestructurablePathTokens
import ratpack.handling.Context

/**
 * User: danielwoods
 * Date: 8/7/13
 */
class ContextExtensions {
  static DestructurablePathTokens getBetterPathTokens(Context context) {
    new DestructurablePathTokens(context.pathTokens)
  }

}
