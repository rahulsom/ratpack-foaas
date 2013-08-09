package app

/**
 * User: danielwoods
 * Date: 8/9/13
 */
class DestructurablePathTokens {
  Map tokens
  DestructurablePathTokens(Map tokens) {
    this.tokens = tokens
  }
  public String getAt(idx) {
    if (idx == 0) (tokens.p2 ? tokens.p1 : null)?.decodeHtml()
    else if (idx == 1) (tokens.p2 ?: tokens.p1)?.decodeHtml()
    else if (idx == 2) tokens.type
    else throw new IllegalArgumentException("No more things to give you!")

  }
}
