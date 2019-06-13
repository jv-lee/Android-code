package com.examples.adapter.reader;

import java.io.BufferedReader;

/**
 * @author jv.lee
 * @date 2019/6/13.
 * description：
 */
public class ReaderAdapter implements BReader {

    private ISReader isReader;

    public ReaderAdapter(ISReader isReader) {
        this.isReader = isReader;
    }

    @Override
    public BufferedReader getBReader() {
        return new BufferedReader(isReader.getISReader());
    }
}
