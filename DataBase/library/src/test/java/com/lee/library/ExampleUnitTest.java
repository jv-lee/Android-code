package com.lee.library;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        StringBuilder builder = new StringBuilder();
        builder.append("abcd");

        System.out.println(builder.substring(0,builder.length()-1));
    }
}