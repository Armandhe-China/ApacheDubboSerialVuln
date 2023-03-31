//package com.arliya.dubbo.main;
//
//import com.caucho.hessian.io.*;
//
//public class NoWriteReplaceSerializerFactory extends SerializerFactory {
//
//    public NoWriteReplaceSerializerFactory() {
//    }
//
//    public Serializer getObjectSerializer(Class<?> cl) throws HessianProtocolException {
//        return super.getObjectSerializer(cl);
//    }
//
//    public Serializer getSerializer(Class cl) throws HessianProtocolException {
//        Serializer serializer = super.getSerializer(cl);
//        return (Serializer)(serializer instanceof WriteReplaceSerializer ? UnsafeSerializer.create(cl) : serializer);
//    }
//
//}
