package app

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class WebSocketsSpec extends FunctionalSpec {

    def "fucks are broadcast via websockets"() {
        when:
        def uri = new URI(app.address.toString() + "ws")
        def wsClient = new RecordingWebSocketClient(uri)
        wsClient.connectBlocking()

        then:
        with(get("off/to/from")) {
            headers["content-type"] == "text/plain;charset=UTF-8"
            body.text.contains "Fuck off, to."
        }


        and:
        def json = wsClient.received.poll(2, TimeUnit.SECONDS)
        def message = readJson(json)
        message.get("message").asText() == "Fuck off, to."
        message.get("subtitle").asText() == "- from"

        when:
        wsClient.closeBlocking()

        then:
        if (wsClient.exception != null) {
            throw wsClient.exception
        }
    }

    def JsonNode readJson(String message) {
        new ObjectMapper().reader().readTree(message)
    }

    @CompileStatic
    static class RecordingWebSocketClient extends WebSocketClient {

        Exception exception

        final LinkedBlockingQueue<String> received = new LinkedBlockingQueue<String>()

        RecordingWebSocketClient(URI serverURI) {
            super(serverURI)
        }

        @Override
        void onOpen(ServerHandshake handshakedata) {

        }

        @Override
        void onMessage(String message) {
            received.put message
        }

        @Override
        void onClose(int code, String reason, boolean remote) {
            if (remote) {
                exception = new Exception("Server initiated close: $code $reason")
            }
        }

        @Override
        void onError(Exception ex) {
            exception = ex
        }
    }
}
