package app

import com.google.inject.AbstractModule
import groovy.text.SimpleTemplateEngine
import static app.FoaasWebSocketBroadcaster._ as BROADCASTER

class FoaasModule extends AbstractModule {

  static int i = 0
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

    bind(FuckOffService).toInstance(new FuckOffService(properties, new SimpleTemplateEngine()))
  }
}
