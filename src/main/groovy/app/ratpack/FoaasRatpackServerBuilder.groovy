package app.ratpack

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import ratpack.handling.Handler
import ratpack.launch.HandlerFactory
import ratpack.launch.LaunchConfig
import ratpack.launch.LaunchException
import ratpack.server.RatpackServer
import ratpack.server.internal.NettyRatpackService
import ratpack.server.internal.ServiceBackedServer
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
