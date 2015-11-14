package app

import com.google.inject.AbstractModule
import groovy.text.SimpleTemplateEngine

class FoaasModule extends AbstractModule {

  final URL resource

  FoaasModule(URL resource) {
    this.resource = resource
  }

  @Override
  protected void configure() {
    def properties = new Properties()
    resource.openStream().withStream {
      properties.load(it)
    }

    bind(FoaasBroadcaster)
    bind(FuckOffService).toInstance(new FuckOffService(properties, new SimpleTemplateEngine()))
    bind(FuckOffRenderer)
  }
}
