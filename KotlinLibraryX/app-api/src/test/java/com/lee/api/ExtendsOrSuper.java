package com.lee.api;

/**
 *
 * @author jv.lee
 * @date 3/29/21
 */
class ExtendsOrSuper {
    private class View {
    }

    private class TextView extends View {

    }

    private class ViewBind<V extends View> {

    }

    private void test1() {
        ViewBind<TextView> viewBind = new ViewBind<>();
        ViewBind<? super TextView> view = new ViewBind<View>();
    }


    public static class One {
        //静态属性和静态方法是否可以被重写？以及原因？
        public static String one_1 = "one";
        public static void oneFn() {
            System.out.println("oneFn");
        }
    }

    public static class Two extends One {

        public static String one_1 = "two";

        public static void oneFn() {

            System.out.println("TwoFn");
        }

    }


}
