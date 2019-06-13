package com.examples.designpatterns.factory;

import com.examples.designpatterns.api.Api;
import com.examples.designpatterns.imple.ApiImpl;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class SimpleFactory {
    public static Api create(){
        return new ApiImpl();
    }
}
