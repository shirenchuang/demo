package com.src.core.remoting.impl.netty_http.client;

import com.src.core.model.RpcRequest;
import com.src.core.remoting.common.ConnectClient;
import com.src.core.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @Description NettyHttp 连接客户端
 * @Author shirenchuang
 * @Date 2019/7/25 10:18 AM
 **/
public class NettyHttpConnectClient extends ConnectClient {


    private EventLoopGroup group;
    private Channel channel;

    private String address;
    private String host;

    //加入序列化类型
    private Serializer serializer;

    @Override
    public void init(String address,Serializer serializer) throws Exception {
        this.serializer = serializer;
        if (!address.toLowerCase().startsWith("http")) {
            address = "http://" + address;	// IP:PORT, need parse to url
        }

        this.address = address;
        URL url = new URL(address);
        this.host = url.getHost();
        int port = url.getPort()>-1?url.getPort():80;


        this.group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(0,0,10, TimeUnit.MINUTES))
                                .addLast(new HttpClientCodec())
                               //添加HttpObjectAggregator解密器，其作用是将多个消息转换为单一的FullHttpRequest或者FullHttpResponse，
                                //解码器在每个HTTP消息中会生成多个消息对象：有1、HttpRequest/HttpResponse;2、HttpContent；3、LastHttpContent；
                                .addLast(new HttpObjectAggregator(5*1024*1024))
                                .addLast(new NettyHttpClientHandler(serializer));
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        this.channel = bootstrap.connect(host, port).sync().channel();

        // valid
        if (!isValidate()) {
            close();
            return;
        }

        ConnectClient.logger.debug(">>>>>>>>>>> netty client proxy, connect to server success at host:{}, port:{}", host, port);
    }

    @Override
    public void send(RpcRequest rpcRequest) throws Exception {
        //序列化
        byte[] requestBytes = serializer.serialize(rpcRequest);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, new URI(address).getRawPath(), Unpooled.wrappedBuffer(requestBytes));
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //同步调用
        this.channel.writeAndFlush(request).sync();
    }

    @Override
    public void close() {
        if (this.channel!=null && this.channel.isActive()) {
            this.channel.close();		// if this.channel.isOpen()
        }
        if (this.group!=null && !this.group.isShutdown()) {
            this.group.shutdownGracefully();
        }
        ConnectClient.logger.debug(">>>>>>>>>>>  netty client close.");
    }

    @Override
    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

}
