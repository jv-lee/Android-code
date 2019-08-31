package com.lee.okhttprequset.connection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019-08-31
 * @description 连接池
 */
public class ConnectionPool {

    private static final String TAG = "ConnectionPool";

    /**
     * 连接池的存放对象
     * 队列，专门去存放（连接对象）
     */
    private Deque<HttpConnection> httpConnectionDeque = new ArrayDeque<>();

    private boolean clearRunnableFlag;

    /**
     * 最大闲置时间
     * 检查的机制
     * 每隔一分钟，就去检查，连接池里面的连接是否可用，如果不可用，就会移除
     */
    private long keepAlive;

    public ConnectionPool() {
        //默认1分钟检查一次连接池
        this(1, TimeUnit.MINUTES);
    }

    public ConnectionPool(long keepAlive, TimeUnit timeUnit) {
        this.keepAlive = timeUnit.toMicros(keepAlive);
    }

    /**
     * 开启一个线程 专门去检查 连接池里面的 （连接对象）
     * 清理连接池里面的 （连接对象）
     */
    private Runnable cleanRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                long nextCheckClearTime = clean(System.currentTimeMillis());

                //while（ture）结束
                if (-1 == nextCheckClearTime) {
                    return;
                }

                if (nextCheckClearTime > 0) {
                    //等待一段时间后再去检查
                    synchronized (ConnectionPool.this) {
                        try {
                            ConnectionPool.this.wait(nextCheckClearTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };

    /**
     * 清理 那些 连接对象
     *
     * @param currentTime
     */
    private long clean(long currentTime) {
        long idleRecordSave = -1;

        synchronized (this) {
            //遍历连接池
            Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
            while (iterator.hasNext()) {
                HttpConnection httpConnection = iterator.next();

                // TODO 添加了一个连接对象，超过了（最大闲置时间） 就会移除这个连接对象

                //获取闲置时间
                long idleTime = currentTime - httpConnection.lastUseTime;

                //大于最大允许的闲置时间 一分钟
                if (idleTime > keepAlive) {
                    //移除
                    iterator.remove();

                    //关闭Socket
                    httpConnection.closeSocket();

                    // 清理 连接对象
                    continue;
                }

                //得到最长闲置时间（计算）
                if (idleRecordSave < idleTime) {
                    idleRecordSave = idleTime;
                }
            }
            //出来循环之后，idleRecordSave值计算完毕 KeepAlive=60S  60-30
            if (idleRecordSave >= 0) {
                return (keepAlive - idleRecordSave);
            }
        }

        //连接池没有连接对象 返回-1 结束线程池中的任务
        return idleRecordSave;
    }

    /**
     * 线程池 复用的决策
     * 参数1：0                         核心线程数
     * 参数2：MAX_VALUE         线程池中最大值
     * 参数3：60                       单位值
     * 参数4：秒钟                    时/分/秒
     * 参数5：LinkedBlockingQueue 链表方式处理的队列 / SynchronousQueue 同步队列
     * <p>
     * 执行任务大于（核心线程数） 启用（60s闲置时间）
     * 60s闲置时间，没有结束，会一直复用同一个线程
     */
    private Executor threadPoolExecutor =
            new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "ConnectionPool");
                            thread.setDaemon(true);
                            return thread;
                        }
                    });

    /**
     * TODO 添加 （连接对象）---> 连接池里面
     * 一旦put的时候，就要去检查，连接池里面，是否要去清理
     */
    public synchronized void putConnection(HttpConnection httpConnection) {
        //如果没有执行 则开始任务
        if (!clearRunnableFlag) {
            clearRunnableFlag = true;
            threadPoolExecutor.execute(cleanRunnable);
        }
        httpConnectionDeque.add(httpConnection);
    }

    /**
     * TODO 获取 （连接对象） ---> 连接池里面 可用 有效的 （复用）
     */
    public HttpConnection getConnection(String host, int port) {
        Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
        while (iterator.hasNext()) {
            HttpConnection httpConnection = iterator.next();
            if (httpConnection.isConnectionAction(host, port)) {
                //移除
                iterator.remove();

                //代表我们找到了 可以复用的
                return httpConnection;
            }
        }
        return null;
    }

}
