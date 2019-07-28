package com.src.core.client;

import com.src.core.model.RpcRequest;
import com.src.core.remoting.common.ConnectClient;
import com.src.core.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Description 。
 * @Author shirenchuang
 * @Date 2019/7/28 12:27 PM
 **/
public class RpcReferenceBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcReferenceBean.class);



    //序列化类型
    private Serializer serializer;

    private Class<?> iface;

    private  String version;

    private String accessToken;

    private String address ;

    //通信类型
    private ConnectClient connectClient ;

    private void initClient() throws Exception {
        connectClient .init(address,Serializer.SerializeEnum.HESSIAN.getSerialize());
    }

    public RpcReferenceBean(Serializer serializer, Class<?> iface, String version, String accessToken, String address, ConnectClient connectClient) throws Exception {
        this.serializer = serializer;
        this.iface = iface;
        this.version = version;
        this.accessToken = accessToken;
        this.address = address;
        this.connectClient = connectClient;
        initClient();
    }

    //生产代理类:  封装了通信细节
    public Object getObject(){

        return Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), new Class[]{iface}, ( proxy,  method,  args)->{
            String className = method.getDeclaringClass().getName();	// iface.getName()
            String varsion_ = version;
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] parameters = args;

            RpcRequest request = new RpcRequest();
            //请求ID  回调的时候有用
            request.setRequestId(UUID.randomUUID().toString());
            request.setClassName(className);
            request.setMethodName(methodName);
            request.setCreateMillisTime(System.currentTimeMillis());
            request.setParameterTypes(parameterTypes);
            request.setParameters(parameters);
            request.setVersion(varsion_);
            connectClient.send(request);

            return null;

        });
    }

}
