package com.zbt.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @author Frank Zhang
 * @Time 17/4/28.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        if(httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest)httpObject;
            if(HttpMethod.GET.equals(httpRequest.getMethod())) {
                System.out.println("Request uri: " + httpRequest.getUri());
            }
        } else if(httpObject instanceof HttpContent) {
            ByteBuf buf = ((HttpContent)httpObject).content();
            System.out.println("post data:"+buf.toString(io.netty.util.CharsetUtil.UTF_8));
        }


        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        ChannelFuture f = ctx.writeAndFlush(response);
        f.addListener(ChannelFutureListener.CLOSE);
    }
}
