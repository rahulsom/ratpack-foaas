package ext

import app.FuckOff
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import groovy.xml.MarkupBuilder

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

  static String toXml(FuckOff f) {
    def sw = new StringWriter()
    def builder = new MarkupBuilder(sw)
    builder {
      "message" f.message
      "subtitle" f.subtitle
    }
    sw.toString()
  }
}
