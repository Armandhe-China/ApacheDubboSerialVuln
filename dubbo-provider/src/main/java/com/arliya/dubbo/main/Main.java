package com.arliya.dubbo.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.in.read();
    }
}
