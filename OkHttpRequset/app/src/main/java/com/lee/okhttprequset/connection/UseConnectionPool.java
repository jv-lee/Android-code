package com.lee.okhttprequset.connection;

import android.util.Log;

/**
 * @author jv.lee
 * @date 2019-09-01
 * @description 使用连接池 连接对象发起请求
 */
public class UseConnectionPool {

    private static final String TAG = "UseConnectionPool";

    private ConnectionPool connectionPool = new ConnectionPool();

    public void useConnectionPool(String host, int port) {
        //模拟
        HttpConnection connection = connectionPool.getConnection(host, port);
        if (connection == null) {
            connection = new HttpConnection(host, port);
            Log.d(TAG, "useConnectionPool: 连接池中没有连接对象，实例化一个连接对象...");
        }else{
            Log.d(TAG, "useConnectionPool: 复用连接池中的连接对象...");
        }

        //模拟请求 记录最后使用时间
        connection.lastUseTime = System.currentTimeMillis();
        connectionPool.putConnection(connection);
        Log.d(TAG, "useConnectionPool: 给服务器发送请求>>>>>>");
    }
}
