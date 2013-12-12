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

  def "content negotiation"() {
    when:
    request.header("Accept", "*/*")

    then:
    getText("off/to/from") == "Fuck off, to. - from"
  }

  def "Content Negotiation JSON"() {
    when:
    request.header("Accept", "application/json")

    then:
    with(get("off/to/from").jsonPath()) {
      get("message") == "Fuck off, to."
      get("subtitle") == "- from"
    }
  }

  def "Content Negotiation text/HTML"() {
    when:
    request.header("Accept", "text/html")

    then:
    with(get("off/to/from")) {
      contentType == "text/html;charset=UTF-8"
      body.asString().contains "Fuck off, to."
    }
  }

  def "Content Negotiation pdf"() {
    when:
    request.header("Accept", "application/pdf")

    then:
    with(get("off/to/from")) {
      contentType == "application/pdf"
      body.asString().startsWith "%PDF"
    }
  }

  def "handles unknowns"() {
    when:
    get("foo/bar/baasdfa")

    then:
    response.statusCode == 404
  }

  def "pathtokens are properly encoded"() {
    when:
    request.header("Accept", "text/html")

    then:
    with(get("blink182/generation/fucking%20heart")) {
      !body.asString().contains("%20")
    }
  }

  def "variant text is produced by #path"() {
    when:
    request.header("Accept", "*/*")

    then:
    getText(path) == expectedText

    where:
    path                      | expectedText
    "keepcalm/to/from"        | "Keep Calm to and Fuck Off - from"
    "steviewonder/to/from"    | "I just called, to say..........to FUCK OFF! - from"
    "malcolmtucker/from"      | "I will tear your fucking skin off, I will wear it to your mother's birthday party and rub your nuts up and down her leg whilst whistling Bohemian fucking Rhapsody. Right? - from"
    "justinbieber/from"       | "There's gonna be times in your life when people say you can't do something. And there's gonna be times in your life when people say that you can't live your dreams. This is what I tell them: FUCK OFF! - from"
    "swearengen/E.B./Al"      | "I will profane your fucking remains, E.B.! - Al"
    "arthur/to/from"          | "How shall to fuck off, O Lord? - from"
    "ceccoangiolieri/to/from" | "to, s'i' so' buon begolardo, - tu me ne tien' ben la lancia a le reni; - s'i' desno con altrui, e tu vi ceni; - s'io mordo 'l grasso, e tu vi sughi el lardo; - s'io cimo 'l panno, e tu vi freghi el cardo. - from"
    "boehner/from"            | "While thousands of programs and employees were deemed \"non-essential\" and suspended or furloughed, one of the few government costs deemed essential to maintain during the  shutdown has been the Congressional gym. Oh no, please, don't get out of the pool, Mr. Boehner. This fuck you is waterproof. - from"
  }

  def cleanup() {
    aut.stop()
  }
}
