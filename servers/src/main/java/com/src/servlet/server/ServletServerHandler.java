package com.src.servlet.server;

import com.src.core.model.RpcRequest;
import com.src.core.model.RpcResponse;
import com.src.core.remoting.invoker.provider.RpcProviderFactory;
import com.src.core.utils.RpcException;
import com.src.core.utils.ThrowableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description 接受client的请求;
 * @Author shirenchuang
 * @Date 2019/7/26 5:01 PM
 **/
public class ServletServerHandler {
    private static Logger logger = LoggerFactory.getLogger(ServletServerHandler.class);

    // 提供者工厂类
    private RpcProviderFactory rpcProviderFactory;


    public ServletServerHandler(RpcProviderFactory rpcProviderFactory) {
        this.rpcProviderFactory = rpcProviderFactory;
    }

    public void handler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RpcRequest rpcRequest = null;
        try {
             rpcRequest  = parseRequest(request);
        } catch (Exception e) {
            // 把异常写到response中返回
            writeResponse(response, ThrowableUtil.toString(e).getBytes());
            return;
        }
        //反射调用 接口
        RpcResponse rpcResponse = rpcProviderFactory.invokeService(rpcRequest);
        // response序列化返回
        byte[] responseBytes = rpcProviderFactory.getSerializer().serialize(rpcResponse);
        writeResponse(response, responseBytes);

    }

    /**
     * write response
     */
    private void writeResponse(HttpServletResponse response, byte[] responseBytes) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        OutputStream out = response.getOutputStream();
        out.write(responseBytes);
        out.flush();
    }


    // 解析请求
    private RpcRequest parseRequest(HttpServletRequest request) throws Exception {
        //反序列化请求参数
        byte[] requestBytes = readBytes(request);
        if (requestBytes == null || requestBytes.length==0) {
            throw new RpcException("rpc 请求参数为空");
        }

        return (RpcRequest)rpcProviderFactory.getSerializer().deserialize(requestBytes, RpcRequest.class);
    }




    /**
     * read bytes from http request
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static final byte[] readBytes(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
        InputStream is = request.getInputStream();
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return new byte[] {};
    }


    public RpcProviderFactory getRpcProviderFactory() {
        return rpcProviderFactory;
    }
}
