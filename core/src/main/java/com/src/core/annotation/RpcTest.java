package com.src.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 打上这个标记, 启动的时候会把这写引用一起放到 一个 ConcurrentHansMap中保存
 * @Author shirenchuang
 * @Date 2019/7/26 5:48 PM
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcTest {
}
