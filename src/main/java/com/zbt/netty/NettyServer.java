package com.zbt.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author Frank Zhang
 * @Time 17/4/28.
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        int port = 8080;
        try {
                int nettyThreadCount = 32;
                // http server.
                NioEventLoopGroup workerGroup = new NioEventLoopGroup(nettyThreadCount);
                ServerBootstrap httpBootstrap = new ServerBootstrap();
                httpBootstrap.group(workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new HttpChannelInitializer());

                ChannelFuture future = httpBootstrap.bind(new InetSocketAddress(port));
                future.sync();

                if (future.isSuccess()) {
                    System.out.println("Server HTTP port bound to " + port);
                } else {
                    System.out.println("Bound to " + port + " attempt failed " + future.cause());
                    throw future.cause();
                }

            System.out.println("search jetty server started in " + (System.currentTimeMillis() - startTime) / 1000 + "s");
        } catch(Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static class HttpChannelInitializer extends ChannelInitializer<Channel> {
        public HttpChannelInitializer() {
        }

        @Override
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new HttpServerCodec());
            pipeline.addLast(new HttpRequestDecoder());
//            pipeline.addLast(new StringEncoder(Charset.forName("GBK")));
//            pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
//            pipeline.addLast(new HttpResponseEncoder());
            pipeline.addLast(new HttpServerHandler());
        }
    }
}
