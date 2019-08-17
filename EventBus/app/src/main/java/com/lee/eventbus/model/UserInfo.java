package com.lee.eventbus.model;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description
 */
public class UserInfo {
    private String name;
    private String age;

    public UserInfo(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
