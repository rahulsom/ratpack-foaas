package app.ratpack

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import org.ratpackframework.handling.Handler
import org.ratpackframework.launch.HandlerFactory
import org.ratpackframework.launch.LaunchConfig
import org.ratpackframework.launch.LaunchException
import org.ratpackframework.server.RatpackServer
import org.ratpackframework.server.internal.NettyRatpackService
import org.ratpackframework.server.internal.ServiceBackedServer
/**
 * User: danielwoods
 * Date: 9/25/13
 */
class FoaasRatpackServerBuilder {

  private FoaasRatpackServerBuilder() {}

  public static RatpackServer build(LaunchConfig launchConfig) {
    ChannelInitializer<SocketChannel> channelInitializer = buildChannelInitializer(launchConfig)
    NettyRatpackService service = new NettyRatpackService(launchConfig, channelInitializer)
    new ServiceBackedServer(service, launchConfig)
  }

  private static ChannelInitializer<SocketChannel> buildChannelInitializer(LaunchConfig launchConfig) {
    return new FoaasRatpackChannelInitializer(launchConfig, createHandler(launchConfig));
  }

  private static Handler createHandler(LaunchConfig launchConfig) {
    HandlerFactory handlerFactory = launchConfig.getHandlerFactory();
    try {
      return handlerFactory.create(launchConfig);
    } catch (Exception e) {
      throw new LaunchException("Could not create handler via handler factory: " + handlerFactory.getClass().getName(), e);
    }
  }
}
