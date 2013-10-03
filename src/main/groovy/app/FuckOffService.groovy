package app

import groovy.text.SimpleTemplateEngine

class FuckOffService {

  private static final String EXAMPLE_FROM_NAME = 'Dave'
  private static final String EXAMPLE_TO_NAME = 'Mike'
  private static final String MESSAGE_PROPERTY_SUFFIX = '.message'
  private static final String SUBTITLE_PROPERTY_SUFFIX = '.subtitle'
  private static final String PROPERTY_PREFIX = 'fuckOff.'

  private final Map<String, FuckOffApiResource> foaasResources = [:]
  private final SimpleTemplateEngine templateEngine

  FuckOffService(Properties messages, SimpleTemplateEngine templateEngine) {
    this.templateEngine = templateEngine

    def exampleFuckOffParams = [from: EXAMPLE_FROM_NAME, to: EXAMPLE_TO_NAME]
    messages.each { property ->
      if (property.key.endsWith(MESSAGE_PROPERTY_SUFFIX)) {
        def key = property.key.replace(PROPERTY_PREFIX, '').replace(MESSAGE_PROPERTY_SUFFIX, '')
        def message = messages.getProperty(PROPERTY_PREFIX + key + MESSAGE_PROPERTY_SUFFIX)
        def subtitle = messages.getProperty(PROPERTY_PREFIX + key + SUBTITLE_PROPERTY_SUFFIX)

        def uri = "/$key" + ((message + subtitle).contains('$to')?'/$to':'') + '/$from'
        def exampleUri = render(uri, exampleFuckOffParams)
        def exampleFuckOff = new FuckOff(
                render(message, exampleFuckOffParams),
                render(subtitle, exampleFuckOffParams))

        def foaasResource = new FuckOffApiResource(uri, message, subtitle, exampleUri, exampleFuckOff)
        this.foaasResources.put(key, foaasResource)
      }
    }
  }

  FuckOff get(String key, String from, String to) {
    def fuckOffResource = foaasResources.get(key)
    def fuckOff = null

    if (fuckOffResource) {
      def params = [from: from, to: to]
      def message = render(fuckOffResource.message, params)
      def subtitle = render(fuckOffResource.subtitle, params)
      fuckOff = new FuckOff(message, subtitle)
    }

    return fuckOff
  }

  private String render(String template, Map<?, ?> params) {
    template ? templateEngine.createTemplate(template).make(params) : null
  }

  List<FuckOffApiResource> getApi() {
    foaasResources.values().asList()
  }

}
