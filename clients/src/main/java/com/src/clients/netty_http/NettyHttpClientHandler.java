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

        //关闭连接
        //两个关闭的区别 https://emacsist.github.io/2018/04/27/%E7%BF%BB%E8%AF%91netty4%E4%B8%AD-ctx.close-%E4%B8%8E-ctx.channel.close-%E7%9A%84%E5%8C%BA%E5%88%AB/
        ctx.channel().close();
        //ctx.close();

    }









}
