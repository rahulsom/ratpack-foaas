package app

import groovy.text.SimpleTemplateEngine

class FuckOffService {

  private static final Map EXAMPLE_PARAMS = [from: 'Dave', to: 'Mike']

  private final Map<String, FuckOffApiResource> foaasResources = [:]
  private final SimpleTemplateEngine templateEngine

  FuckOffService(Properties messages, SimpleTemplateEngine templateEngine) {
    this.templateEngine = templateEngine
    Map m = messages.keySet().collect{it.split('\\.').reverse() as Stack}.inject([:]) {m,v->c v,m;m}
    messages.each {k,v->Eval.x m,"x.$k='${v.replaceAll("'", "\\\\'")}'"}
    m.fuckOff.each { String key, Map value ->
      def (msg, subtitle, uri) = destructure(key, value)
      this.foaasResources."$key" = [uri, msg, subtitle, render(uri, EXAMPLE_PARAMS), [msg, subtitle] as FuckOff] as FuckOffApiResource
    }
  }

  static final def c = { Stack stack, Map last ->
    def key = stack.pop()
    if (!last[key]) last[key] = [:]
    last = (Map)last[key]
    if (stack) FuckOffService.c stack, last
  }

  static final def destructure = { String key, Map map ->
    def uri = map.message.contains("\$to") ? "/$key/\$to/\$from" : "/$key/\$from"
    [map.message, map.subtitle, uri]
  }

  FuckOff get(String key, String from, String to) {
    def fuckOffResource = foaasResources."$key"
    def fuckOff = null

    if (fuckOffResource) {
      def params = [from: from, to: to]
      def message = render(fuckOffResource.message, params)
      def subtitle = render(fuckOffResource.subtitle, params)
      fuckOff = new FuckOff(message, subtitle)
    }

    fuckOff
  }

  private String render(String template, Map<?, ?> params) {
    template ? templateEngine.createTemplate(template).make(params) : null
  }

  List<FuckOffApiResource> getApi() {
    foaasResources.values() as List
  }

}
