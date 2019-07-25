package com.src.clients.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 通讯客户端抽象类
 * @Author shirenchuang
 * @Date 2019/7/25 10:11 AM
 **/
public  abstract class ConnectClient {

    protected static transient Logger logger = LoggerFactory.getLogger(ConnectClient.class);




    public abstract void init(String address) throws Exception;


    //发起请求
    public abstract void send(RpcRequest rpcRequest) throws Exception ;



    public abstract void close();


    public abstract boolean isValidate();







}
