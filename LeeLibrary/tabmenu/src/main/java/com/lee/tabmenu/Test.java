package com.lee.tabmenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Test {

    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("Heading");
        list.add("item1");
        list.add("item2");
        list.add("item3");
        list.add("item4");
        list.add("SubHeading");
        list.add("item5");
        list.add("item6");
        list.add("item7");
        list.add("item8");


        String remove = list.remove(9);
        list.add(5,remove);


        for (String s : list) {
            System.out.println(s);
        }
    }
}
