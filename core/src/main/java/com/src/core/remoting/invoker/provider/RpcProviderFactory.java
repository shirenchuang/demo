package com.src.core.remoting.invoker.provider;

import com.src.core.model.RpcRequest;
import com.src.core.model.RpcResponse;
import com.src.core.serialize.Serializer;
import com.src.core.utils.ThrowableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 服务提供者  反射调用
 *      这个工厂类要  在提供者所在项目中被spring管理
 * @Author shirenchuang
 * @Date 2019/7/26 5:20 PM
 **/
public class RpcProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(RpcProviderFactory.class);

    //序列化类型
    private Serializer serializer;

    public RpcProviderFactory(Serializer serializer) {
        this.serializer = serializer;
    }

    //持有所有服务提供者的Service 引用
    private static ConcurrentHashMap<String,Object> serviceHolder = new ConcurrentHashMap();


    /**
     * 反射调用 提供者的方法
     * @param request
     * @return
     */
    public RpcResponse invokeService(RpcRequest request){
        RpcResponse response = new RpcResponse();
        response .setRequestId(request.getRequestId());

        //找到需要被调用到服务提供者
        String serviceName = makeServiceKey(request.getClassName(),request.getVersion());
        Object serviceBean = serviceHolder.get(serviceName);

        //反射调用
        try {
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();

            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            Object result = method.invoke(serviceBean, parameters);
            response.setResult(result);
        }catch (Throwable t){
            // catch error
            logger.error("rpc 服务 调用异常", t);
            response.setErrorMsg(ThrowableUtil.toString(t));
        }
        return response;
    }

    //把提供者服务保存起来
    public void addService(String className,String version,Object serviceBean){
        String serviceKey = makeServiceKey(className, version);
        serviceHolder.put(serviceKey, serviceBean);
        logger.info(">>>>>>>>>>> RPC, provider factory add service success. serviceKey = {}, serviceBean = {}", serviceKey, serviceBean.getClass());

    }

    public static String makeServiceKey(String iface, String version){
        String serviceKey = iface;
        if (version!=null && version.trim().length()>0) {
            serviceKey += "#".concat(version);
        }
        return serviceKey;
    }


    public Serializer getSerializer() {
        return serializer;
    }


}
