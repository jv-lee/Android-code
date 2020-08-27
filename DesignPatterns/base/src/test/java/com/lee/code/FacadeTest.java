package com.lee.code;

import com.lee.code.structure.facade.Facade;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/26.
 * @description 外观模式测试用例
 */
public class FacadeTest {
    @Test
    public void test() {
        //案例1
        Facade facade = new Facade();
        facade.method();
    }
}