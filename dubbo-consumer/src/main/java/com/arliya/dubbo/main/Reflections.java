package com.arliya.dubbo.main;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Reflections{
    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) throws Exception{
        Field field=null;
        Class cl = obj.getClass();
        while (cl != Object.class){
            try{
                field = cl.getDeclaredField(fieldName);
                if(field!=null){
                    break;}
            }
            catch (Exception e){
                cl = cl.getSuperclass();
            }
        }
        if (field==null){
            System.out.println(obj.getClass().getName());
            System.out.println(fieldName);
        }
        field.setAccessible(true);
        field.set(obj,fieldValue);
    }

    public static <T> T createWithoutConstructor(Class<T> classToInstantiate) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }

    public static <T> T createWithConstructor(Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
    }
}
