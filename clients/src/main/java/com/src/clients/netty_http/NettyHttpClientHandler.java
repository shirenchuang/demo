package com.src.clients.netty_http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 10:34 AM
 **/
public class NettyHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {

        System.out.println("收到回复啦："+msg.toString());
        byte[] responseBytes = ByteBufUtil.getBytes(msg.content());
        String rsp = new String(responseBytes,"UTF-8");
        System.out.println("收到回复啦："+rsp);

    }







}
