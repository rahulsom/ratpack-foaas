package app

import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import ratpack.test.ApplicationUnderTest
import ratpack.test.CloseableApplicationUnderTest
import ratpack.test.http.TestHttpClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class FunctionalSpec extends Specification {

  @Shared
  @AutoCleanup
  ApplicationUnderTest app = new CloseableApplicationUnderTest() {
    CloseableApplicationUnderTest app

    @Override
    void close() {
      app?.close()
    }

    @Override
    URI getAddress() {
      if (app == null) {
        app = new GroovyRatpackMainApplicationUnderTest()
      }
      app.address
    }
  }

  @Delegate
  TestHttpClient client = app.httpClient

  def setup() {
    client.resetRequest()
  }

}
