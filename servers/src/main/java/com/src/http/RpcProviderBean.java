package com.src.http;

import com.src.core.annotation.RpcTest;
import com.src.core.remoting.invoker.provider.RpcProviderFactory;
import com.src.core.serialize.Serializer;
import com.src.core.serialize.impl.HessianSerializer;
import com.src.servlet.server.ServletServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description   提供者
 * @Author shirenchuang
 * @Date 2019/7/26 5:46 PM
 **/
@Component
public class RpcProviderBean implements InitializingBean, DisposableBean, ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(RpcProviderBean.class);

    private static ServletServerHandler servletServerHandler;

    //序列化方式,后面可以换成配置
    private Serializer serializer = new HessianSerializer();


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init(){
        RpcProviderFactory rpcProviderFactory = new RpcProviderFactory(serializer);
        servletServerHandler = new ServletServerHandler(rpcProviderFactory);
    }






    @Override
    public void destroy() throws Exception {

    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //找所有 @RpcTest 注解 https://blog.csdn.net/joshua1830/article/details/52117250
        // 根容器为Spring容器
        if(event.getApplicationContext().getParent()==null){
            Map<String,Object> beans = event.getApplicationContext().getBeansWithAnnotation(RpcTest.class);
            for(Object bean:beans.values()){
                servletServerHandler.getRpcProviderFactory().addService(bean.getClass().getInterfaces()[0].getName(),null,bean);
                System.err.println(bean==null?"null":bean.getClass().getName());
            }
            System.err.println("=====ContextRefreshedEvent====="+event.getSource().getClass().getName());
        }
    }

    public  ServletServerHandler getServletServerHandler() {
        return servletServerHandler;
    }
}
