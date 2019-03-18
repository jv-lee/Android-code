package com.lee.library.handler;

public class Message {

    Handler target;

    public Object obj;

    public int what;

    public Message() {

    }

    @Override
    public String toString() {
        return obj.toString();
    }
}
