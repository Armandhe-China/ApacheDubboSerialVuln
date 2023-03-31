package com.arliya.dubbo.provider;

import com.arliya.dubbo.api.School;

public class SchoolImpl implements School {
    public String name;
    @Override
    public String getName() {
        return "shabi";
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
