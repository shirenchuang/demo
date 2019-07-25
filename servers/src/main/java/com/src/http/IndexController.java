package com.src.http;

import com.src.core.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description TODO
 * @Author shirenchuang
 * @Date 2019/7/25 1:57 PM
 **/

@Controller
public class IndexController {

    @RequestMapping("/api")
    @ResponseBody
    public ReturnT<String> index() {

        ReturnT returnT = ReturnT.SUCCESS;
        returnT.setContent("OJBK、收到请求");

        return returnT;
    }


}
