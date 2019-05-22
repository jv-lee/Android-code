package com.lee.code.andfix.web;

import com.lee.code.andfix.Replace;

/**
 * @author jv.lee
 * @date 2019-05-22
 */
public class Caclutor {
    @Replace(clazz = "com.lee.code.andfix.Caclutor",method = "caculator")
    public int caculator(){
        return 10;
    }

}
