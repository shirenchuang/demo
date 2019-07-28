package com.src.core.utils;


public class RpcException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

}