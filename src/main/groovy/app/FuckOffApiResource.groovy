package app

class FuckOffApiResource {

  final String uri
  final String message
  final String subtitle
  final String exampleUri
  final FuckOff exampleFuckOff

  FuckOffApiResource(String uri, String message, String subtitle, String exampleUri, FuckOff exampleFuckOff) {
    this.uri = uri
    this.message = message
    this.subtitle = subtitle
    this.exampleUri = exampleUri
    this.exampleFuckOff = exampleFuckOff
  }

  public String getUri() {
    this.uri.replace('$to', ':to').replace('$from', ':from')
  }

  public String getMessageTemplate() {
    "$message $subtitle".replace('$to', ':to').replace('$from', ':from')
  }

  public String getExampleMessage() {
    return "$exampleFuckOff.message $exampleFuckOff.subtitle"
  }

  public String getExampleMp3Uri() {
    "/mp3$exampleUri"
  }
}
