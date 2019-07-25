package com.src.clients.common;

import java.io.Serializable;

/**
 * @Description 通讯请求参数
 * @Author shirenchuang
 * @Date 2019/7/25 10:56 AM
 **/
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;


    private String requestId;

    public RpcRequest(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
