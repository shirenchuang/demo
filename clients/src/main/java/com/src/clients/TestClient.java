package com.src.clients;

import com.src.clients.common.ConnectClient;
import com.src.clients.common.RpcRequest;
import com.src.clients.netty_http.NettyHttpConnectClient;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 3:09 PM
 **/
public class TestClient {



    public static void main(String[] args) throws Exception {
        ConnectClient connectClient = new NettyHttpConnectClient();
        connectClient.init("http://localhost:8888/api");
        connectClient.send(new RpcRequest("66666"));
    }
}
