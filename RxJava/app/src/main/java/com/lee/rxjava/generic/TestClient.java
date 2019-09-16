package com.lee.rxjava.generic;

/**
 * @author jv.lee
 * @date 2019/9/16.
 * @description
 */
public class TestClient {

    public static void main(String[] args) {

        //上限
        show1(new Test<Person>());
        show1(new Test<Student>());
        show1(new Test<Worker>());

        //下限
        show2(new Test<Person>());
        show2(new Test<Object>());


        //可读模式
        Test<? extends Person> readTest = null;
//        readTest.add(new Person());
//        readTest.add(new Student());
//        readTest.add(new Worker());
        Person person = readTest.get();

        Test<? super Person> writeTest = null;
        writeTest.add(new Person());
        writeTest.add(new Student());
        writeTest.add(new Worker());
        //可写模式下 读取需要强转
        Object object = writeTest.get();
    }


    /**
     * 上限 Person 和 Person所有子类 都可以， 最高类型只能是Person，把更上层的父类限制住了
     *
     * @param test
     */
    public static void show1(Test<? extends Person> test) {

    }

    /**
     * 下限 Person 和 Person所有父类 都可以， 最低只能是Person ， 把更下层的子类都限制住了
     *
     * @param test
     */
    public static void show2(Test<? super Person> test) {

    }

}
