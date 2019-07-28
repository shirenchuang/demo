package com.src.http;

import com.src.core.model.ReturnT;
import com.src.servlet.server.ServletServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 1:57 PM
 **/

@Controller
public class IndexController {

    @Autowired
    private RpcProviderBean rpcProviderBean;

    @RequestMapping("/api")
    @ResponseBody
    public ReturnT<String> index() {

        ReturnT returnT = ReturnT.SUCCESS;
        returnT.setContent("OJBK、收到请求");

        return returnT;
    }


    @RequestMapping("/invoke")
    @ResponseBody
    public void invoke(HttpServletRequest request, HttpServletResponse response) throws IOException {
        rpcProviderBean.getServletServerHandler().handler(request,response);
    }




}
