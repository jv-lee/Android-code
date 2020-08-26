package com.lee.code;

import com.lee.code.structure.adapter.part.ElectricAdapter;
import com.lee.code.structure.adapter.part.ElectricMotor;
import com.lee.code.structure.adapter.part.Motor;
import com.lee.code.structure.adapter.part.OpticalAdapter;
import com.lee.code.structure.adapter.part.OpticalMotor;

import org.junit.Test;

/**
 * @author jv.lee
 * @date 2020/8/26.
 * @description 适配器用例
 */
public class AdapterTest {
    @Test
    public void test() {
        //发动机适配案例
        Motor motor;
        if (true) {
            motor = new ElectricAdapter(new ElectricMotor());
        } else {
            motor = new OpticalAdapter(new OpticalMotor());
        }
        motor.drive();
    }
}