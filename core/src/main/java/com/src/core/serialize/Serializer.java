package com.src.core.serialize;

import com.src.core.serialize.impl.HessianSerializer;

/**
 * @Description 抽象类序列化
 *
 * 模板模式  定义序列化与反序列化方法;
 * 单例模式  用Enum延迟加载  https://www.hollischuang.com/archives/2498
 *
 *
 * @Author shirenchuang
 * @Date 2019/7/25 4:03 PM
 **/
public abstract class Serializer {


    public abstract <T> byte[] serialize(T obj);

    public abstract <T> Object deserialize(byte[] bytes, Class<T> clazz);



    //序列号枚举类
    public enum SerializeEnum{
        HESSIAN(HessianSerializer.class);

        //枚举类必须是 Serializer的子类
        private Class<? extends Serializer> serializerClass;



        SerializeEnum(Class<? extends Serializer> serializerClass) {
            this.serializerClass = serializerClass;
        }

        public Serializer getSerialize(){
            try {
                return serializerClass.newInstance();
            }  catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
