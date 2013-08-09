import app.FoaasModule
import app.FuckOffService

import static org.ratpackframework.groovy.RatpackScript.ratpack

ratpack {
  modules {
    register new FoaasModule(getClass().getResource("messages.properties"))
  }

  handlers { FuckOffService service ->
    prefix("img") {
      assets "public/img", [] as String[]
    }
    get(":type/:p1/:p2?") {

      def to = (pathTokens.p2 ? pathTokens.p1 : null)?.decodeHtml()
      def from = (pathTokens.p2 ?: pathTokens.p1)?.decodeHtml()

      "send object if available" service.get(pathTokens.type, from, to)

    }

    assets "public", "index.html"
  }
}
