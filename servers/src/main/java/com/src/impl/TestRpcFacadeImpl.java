package com.src.impl;

import com.src.core.annotation.RpcTest;
import com.src.facade.facade.TestRpcFacade;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/26 4:55 PM
 **/
@RpcTest
@Service
public class TestRpcFacadeImpl implements TestRpcFacade {
    @Override
    public String hello(String name) {
        return "hello! "+name;
    }
}
