package com.lee.code.structure.adapter.part;

/**
 * @author jv.lee
 * @date 2020/8/26
 * @description 电能适配器
 */
public class OpticalAdapter implements Motor {

    private OpticalMotor motor;

    public OpticalAdapter(OpticalMotor motor) {
        this.motor = motor;
    }

    @Override
    public void drive() {
        motor.opticalDrive();
    }
}
