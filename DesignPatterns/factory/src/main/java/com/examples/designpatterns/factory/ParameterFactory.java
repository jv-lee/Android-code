package com.examples.designpatterns.factory;

import com.examples.designpatterns.api.Api;
import com.examples.designpatterns.imple.ApiImpl_A;
import com.examples.designpatterns.imple.ApiImpl_B;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class ParameterFactory {

    public static Api createApi(int parameter) {
        Api api = null;
        switch (parameter) {
            case 1:
                api = new ApiImpl_A();
                break;
            case 2:
                api = new ApiImpl_B();
                break;
            default:
        }
        return api;
    }

}
