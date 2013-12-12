package app.ratpack

import com.google.common.util.concurrent.ListeningExecutorService
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.FullHttpRequest
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory
import ratpack.handling.Handler
import ratpack.launch.LaunchConfig
import ratpack.server.internal.NettyHandlerAdapter

import static app.FoaasWebSocketBroadcaster._ as BROADCASTER
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST
import static io.netty.handler.codec.http.HttpHeaders.Names.UPGRADE
/**
 * User: danielwoods
 * Date: 9/25/13
 */
class FoaasHandlerAdapter extends NettyHandlerAdapter {

  WebSocketServerHandshaker handshaker

  private static final String WEBSOCKET_UPGRADE_KEY = "websocket"
  private static final String WEBSOCKET_PATH = "/ws"

  FoaasHandlerAdapter(Handler handler, LaunchConfig launchConfig, ListeningExecutorService blockingExecutorService) {
    super(handler, launchConfig, blockingExecutorService)
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    if (msg instanceof FullHttpRequest) {
      FullHttpRequest req = msg as FullHttpRequest

      if (req.headers().get(UPGRADE) == WEBSOCKET_UPGRADE_KEY) {
        def wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, false)
        handshaker = wsFactory.newHandshaker(req)
        if (handshaker == null) {
          WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse ctx.channel()
        } else {
          handshaker.handshake ctx.channel(), req
          BROADCASTER.addSubscriber ctx
        }
      } else {
        super.channelRead0 ctx, req
      }
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  private static String getWebSocketLocation(FullHttpRequest req) {
    "ws://${req.headers().get(HOST)}$WEBSOCKET_PATH"
  }
}
