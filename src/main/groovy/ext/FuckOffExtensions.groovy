package ext

import app.FuckOff
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import groovy.xml.MarkupBuilder
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.GetMethod

/**
 * User: danielwoods
 * Date: 8/7/13
 */
class FuckOffExtensions {
  static byte[] toPdf(FuckOff f) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

    Document document = new Document()
    PdfWriter.getInstance(document, outputStream).with {
      setEncryption "fuckoff".bytes, "ownerfuckoff".bytes, ALLOW_PRINTING, (STANDARD_ENCRYPTION_128 | DO_NOT_ENCRYPT_METADATA) as int
      createXmpMetadata()
    }

    document.with {
      open()
      def basis = "Fuck Off"
      addTitle basis
      addSubject basis
      addKeywords basis
      addAuthor "FOaaS"
      addCreator "http://ratpack-foaas.herokuapp.com"

      add new Paragraph("$f.message $f.subtitle")

      close()
    }

    outputStream.toByteArray()
  }

  static byte[] toMp3(FuckOff f) {
    def fuck = f.message.substring(0, Math.min(f.message.length(), 50))
    def from = f.subtitle
    def msg = "$fuck From $from".encodeHtml()
    def get = new GetMethod("http://translate.google.com/translate_tts?tl=en&q=$msg")
    new HttpClient().executeMethod(get)
    new BufferedInputStream(get.getResponseBodyAsStream()).bytes
  }

}
