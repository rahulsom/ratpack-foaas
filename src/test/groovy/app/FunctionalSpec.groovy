package app

import ratpack.groovy.test.LocalScriptApplicationUnderTest
import ratpack.groovy.test.TestHttpClient
import ratpack.groovy.test.TestHttpClients
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class FunctionalSpec extends Specification {

  def aut = new LocalScriptApplicationUnderTest()
  @Delegate
  TestHttpClient client = TestHttpClients.testHttpClient(aut)

  def setup() {
    client.resetRequest()
  }


  def cleanup() {
    aut.stop()
  }
}
