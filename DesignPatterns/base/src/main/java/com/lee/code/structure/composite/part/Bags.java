package com.lee.code.structure.composite.part;

import java.util.ArrayList;

/**
 * @author jv.lee
 * @date 2020/8/31
 * @description
 */
public class Bags implements Articles {

    private String name;
    private ArrayList<Articles> bags = new ArrayList<>();

    public Bags(String name) {
        this.name = name;
    }

    public void add(Articles articles) {
        bags.add(articles);
    }

    public void remove(Articles articles) {
        bags.remove(articles);
    }

    public Articles getChild(int index) {
        return bags.get(index);
    }

    @Override
    public float calculation() {
        float price = 0;
        for (Articles bag : bags) {
            price += bag.calculation();
        }
        return price;
    }

    @Override
    public void show() {
        for (Articles bag : bags) {
            bag.show();
        }
    }
}
