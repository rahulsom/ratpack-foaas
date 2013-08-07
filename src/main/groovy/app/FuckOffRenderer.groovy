package app

import io.netty.buffer.Unpooled
import org.ratpackframework.handling.Context
import org.ratpackframework.render.ByTypeRenderer

import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.Util.with
import static org.ratpackframework.groovy.templating.Template.groovyTemplate

import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter

class FuckOffRenderer extends ByTypeRenderer<FuckOff> {

    public FuckOffRenderer() {
        super(FuckOff)
    }

    @Override
    void render(Context context, FuckOff f) {
        with(context.accepts) {
            type("text/plain") {
                context.response.send "$f.message $f.subtitle"
            }
            type("text/html") {
                context.render groovyTemplate("fuckoff.html", f: f)
            }
            type("application/json") {
                context.response.send toJson(f)
            }
            type("application/pdf") {
                context.response.send("application/pdf", Unpooled.copiedBuffer(toPdf(f)))
            }
        }
    }

    static byte[] toPdf(FuckOff f) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        Document document = new Document()
        PdfWriter writer = PdfWriter.getInstance(document, outputStream)

        // Encrypt and set the password
        writer.setEncryption("fuckoff".bytes, "ownerfuckoff".bytes,
                PdfWriter.ALLOW_PRINTING, PdfWriter.STANDARD_ENCRYPTION_128|PdfWriter.DO_NOT_ENCRYPT_METADATA)
        writer.createXmpMetadata()

        document.open()
        document.addTitle("Fuck OFF")
        document.addSubject("Fuck Off")
        document.addKeywords("Fuck Off")
        document.addAuthor("FOaaS")
        document.addCreator("http://ratpack-foaas.herokuapp.com/")
        document.add(new Paragraph("$f.message $f.subtitle"))
        document.close()

        return outputStream.toByteArray()
    }
}