package com.examples.adapter;

import com.examples.adapter.reader.BReader;
import com.examples.adapter.reader.ReaderAdapter;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：适配器模式
 */
public class ReaderUnitTest {

    @Test
    public void reader() throws IOException {
        FileInputStream fis = new FileInputStream(new File("e:/file.txt"));
        BReader bReader = new ReaderAdapter(new ISReaderImpl(fis));
        BufferedReader reader = bReader.getBReader();
        System.out.println(reader.readLine());
    }
}
