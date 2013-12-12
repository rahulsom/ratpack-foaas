package app.ratpack

import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.ssl.SslHandler
import ratpack.handling.Handler
import ratpack.launch.LaunchConfig
import ratpack.server.Stopper

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLEngine
/**
 * User: danielwoods
 * Date: 9/25/13
 */
class FoaasRatpackChannelInitializer extends ChannelInitializer<SocketChannel> {
  private FoaasHandlerAdapter handlerAdapter;
  private SSLContext sslContext;

  public FoaasRatpackChannelInitializer(LaunchConfig launchConfig, Handler handler, Stopper stopper) {
    ListeningExecutorService blockingExecutorService = MoreExecutors.listeningDecorator(launchConfig.getBackgroundExecutorService());
    this.handlerAdapter = new FoaasHandlerAdapter(stopper, handler, launchConfig, blockingExecutorService);
    this.sslContext = launchConfig.getSSLContext();
  }

  @Override
  public void initChannel(SocketChannel ch) {
    ChannelPipeline pipeline = ch.pipeline()

    if (sslContext) {
      sslContext.createSSLEngine().with { SSLEngine engine ->
        engine.useClientMode = false
        pipeline.addLast "ssl", new SslHandler(engine)
      }
    }

    def $this = this
    pipeline.with {
      addLast "codec-http", new HttpServerCodec()
      addLast "aggregator", new HttpObjectAggregator(65536)
      addLast "handler", $this.handlerAdapter
    }
  }
}
