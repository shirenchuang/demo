package com.src.clients;

import com.src.clients.common.ConnectClient;
import com.src.clients.common.RpcRequest;
import com.src.clients.netty_http.NettyHttpConnectClient;
import com.src.core.serialize.Serializer;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 3:09 PM
 **/
public class TestClient {



    public static void main(String[] args) throws Exception {
        ConnectClient connectClient = new NettyHttpConnectClient();
        connectClient.init("http://localhost:8888/api",Serializer.SerializeEnum.HESSIAN.getSerialize());
        connectClient.send(new RpcRequest("66666"));
    }
}
