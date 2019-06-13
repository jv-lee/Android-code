package com.examples.adapter;

import com.examples.adapter.reader.ISReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class ISReaderImpl implements ISReader {

    private InputStream is;

    public ISReaderImpl(InputStream is) {
        this.is = is;
    }

    @Override
    public InputStreamReader getISReader() {
        return new InputStreamReader(is);
    }
}
