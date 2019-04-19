package com.gionee.gnservice.lib;

public class MyClass {

    public static void main(String[] args) {
        String packageName = "com.lee.code.MainActivity";
        int index = packageName.lastIndexOf(".")+1;
        System.out.print(packageName.substring(index));
    }
}
