package com.src.clients;

import com.src.core.client.RpcReferenceBean;
import com.src.core.remoting.common.ConnectClient;
import com.src.core.remoting.impl.netty_http.client.NettyHttpConnectClient;
import com.src.core.model.RpcRequest;
import com.src.core.serialize.Serializer;
import com.src.facade.facade.TestRpcFacade;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 3:09 PM
 **/
public class TestClient {



    public static void main(String[] args) throws Exception {
        //ConnectClient connectClient = new NettyHttpConnectClient();
        //connectClient.init("http://localhost:8888/api",Serializer.SerializeEnum.HESSIAN.getSerialize());
        //connectClient.send(new RpcRequest("66666"));
        hello2("爸爸");
    }



    //test1
    public static void hello(String name) throws Exception {
        ConnectClient connectClient = new NettyHttpConnectClient();
        connectClient.init("http://localhost:8888/invoke",Serializer.SerializeEnum.HESSIAN.getSerialize());
        RpcRequest request = new RpcRequest();
        request.setRequestId("123");
        request.setClassName("com.src.facade.facade.TestRpcFacade");
        request.setMethodName("hello");
        request.setCreateMillisTime(System.currentTimeMillis());
        Class<?>[] parameterTypes = {String.class};
        request.setParameterTypes(parameterTypes);
        Object[] objects = {name};
        request.setParameters(objects);
        connectClient.send(request);
    }

    //test2 用代理类的方式调用
    public static void hello2(String name) throws Exception {

        TestRpcFacade testRpcFacade = (TestRpcFacade) new RpcReferenceBean(Serializer.SerializeEnum.HESSIAN.getSerialize(),
                TestRpcFacade.class,null,null,"http://localhost:8888/invoke",
                new NettyHttpConnectClient()).getObject();

        testRpcFacade.hello(name);
    }
}
