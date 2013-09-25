package app

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

/**
 * User: danielwoods
 * Date: 9/25/13
 */
enum FoaasWebSocketBroadcaster {
  _

  private volatile List<ChannelHandlerContext> subscriptions = Collections.synchronizedList([])

  public void addSubscriber(ChannelHandlerContext ctx) {
    subscriptions << ctx
  }

  public void removeSubscriber(ChannelHandlerContext ctx) {
    subscriptions.remove ctx
  }

  public void broadcast(String msg) {
    def removeds = []
    subscriptions.each { ctx ->
      if (!ctx.channel().isOpen()) {
        removeds << ctx
        return
      }
      ctx.channel().write new TextWebSocketFrame(msg)
      ctx.flush()
    }
    subscriptions.removeAll removeds
  }
}
