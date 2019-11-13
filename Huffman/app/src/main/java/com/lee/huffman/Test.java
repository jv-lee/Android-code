package com.lee.huffman;

/**
 * @author jv.lee
 * @date 2019-11-14
 * @description
 */
public class Test {

    public static void main(String[] args) {
        //16进制 1111 1111 1011 0110 1100 0001
        int color = 0xFFB6C1;

        /**
         * &运算
         * 0 & 0=0
         * 0 & 1=0
         * 1 & 0=0
         * 1 & 1=1
         */

        //转2进制
        //0xFF = 0000 0000 0000 0000 1111 1111  & 0000 0000 0000 0000 1111 1111 = 2进制 1111 1111 = 255
        int r = (color  >> 16) & 0xFF;
        //0xB6 = 0000 0000 1111 1111 1011 0110 &  0000 0000 0000 0000 1111 1111 = 2进制 1011 0110 = 182
        int g = (color >> 8) & 0xFF;
        //0xC1 = 1111 1111 1011 0110 1100 0001 & 0000 0000 0000 0000 1111 1111 = 2进制  1100 0001 = 193
        int b = color & 0xFF;

        //最终输出10进制
        System.out.println(color);
        System.out.println(r);
        System.out.println(g);
        System.out.println(b);
    }
}
