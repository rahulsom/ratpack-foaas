package ext

import static java.net.URLDecoder.decode as _d
import static java.net.URLEncoder.encode as _e

/**
 * User: danielwoods
 * Date: 7/29/13
 */
class StringExtensions {

  static String decodeHtml(String p, String charEnc = "UTF-8") {
    _d p, charEnc
  }

  static String encodeHtml(String p, String charEnc = "UTF-8") {
    _e p, charEnc
  }
}
