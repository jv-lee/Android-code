package code.lee.code.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jv.lee
 * @date 2019/4/6
 */
public class ThreadPoolManager {

    private static ThreadPoolManager instance;
    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    public static ThreadPoolManager getInstance(){
        if (instance == null) {
            synchronized (ThreadPoolManager.class) {
                if (instance == null) {
                    instance = new ThreadPoolManager();
                }
            }
        }
        return instance;
    }

    private ThreadPoolManager(){
        //最大核心线程数、最大线程数、闲置时间、任务队列、工厂
        threadPoolExecutor = new ThreadPoolExecutor(3,
                Integer.MAX_VALUE,
                15,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(5),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r);
                    }
                });
    }

    public void addTask(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }

    public void removeTask(Runnable runnable) {
        threadPoolExecutor.remove(runnable);
    }

    public ThreadPoolExecutor getExecutor(){
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }
        return null;
    }


}
