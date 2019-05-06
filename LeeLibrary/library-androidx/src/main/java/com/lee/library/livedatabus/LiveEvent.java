package com.lee.library.livedatabus;

/**
 * @author jv.lee
 * @date 2019/4/19
 */
public class LiveEvent<T> {
    private int option;
    private T data;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
