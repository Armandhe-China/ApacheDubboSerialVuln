package com.arliya.dubbo.provider;

import com.arliya.dubbo.api.Person;

public class PersonImpl implements Person {
    public String sayHello(String name) {
        return "Hello" + name;
    }

    public String working(String work) {
        return "我的工作是" + work;
    }

    @Override
    public String getS() {
        return "shabi";
    }

    @Override
    public String setY(Boolean b, String s) {
        return null;
    }
}
