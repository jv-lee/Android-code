package com.lee.code.hermes.process;

public class Person {
    private String name;
    private String password;

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{"+
                "name='"+name+'\''+
                ",password='"+password+'\''+
                "}";
    }
}