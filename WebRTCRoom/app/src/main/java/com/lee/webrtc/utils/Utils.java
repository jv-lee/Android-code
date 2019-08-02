package com.lee.webrtc.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author jv.lee
 * @date 2019-08-02
 * @description
 */
public class Utils {

    private static Utils instance;

    private Utils() {
    }

    private Utils(Context context) {
        mScreenWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public static Utils getInstance(Context context) {
        if (instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils(context);
                }
            }
        }
        return instance;
    }

    public static Utils getInstance() {
        if (instance == null) {
            throw new RuntimeException("未调用 getInstance(Context context) 初始化方法 .");
        }
        return instance;
    }


    /**
     * 获取屏幕宽度
     */
    private static int mScreenWidth;

    public int getWidth(int size) {
        //只有四个人及以下
        if (size <= 4) {
            return mScreenWidth / 2;
        } else {
            return mScreenWidth / 3;
        }
    }

    public int getX(int size, int index) {
        if (size <= 4) {
//            当会议室只有3个人的时候    第三个人的X值偏移
            if (size == 3 && index == 2) {
                return mScreenWidth / 4;
            }
            return (index % 2) * mScreenWidth / 2;
        } else if (size <= 9) {
//            当size 5个人   3  4
            if (size == 5) {
                if (index == 3) {
                    return mScreenWidth / 6;
                }
                if (index == 4) {
                    return mScreenWidth / 2;
                }
            }


            if (size == 7 && index == 6) {
                return mScreenWidth / 3;
            }
            if (size == 8) {
                if (index == 6) {
                    return mScreenWidth / 6;
                }
                if (index == 7) {
                    return mScreenWidth / 2;
                }
            }
            return (index % 3) * mScreenWidth / 3;
        }

        return 0;

    }

    public int getY(int size, int index) {
        if (size < 3) {
            return mScreenWidth / 4;
        } else if (size < 5) {
            if (index < 2) {
                return 0;
            } else {
                return mScreenWidth / 2;
            }
        } else if (size < 7) {
            if (index < 3) {
                return mScreenWidth / 2 - (mScreenWidth / 3);
            } else {
                return mScreenWidth / 2;
            }
        } else if (size <= 9) {
            if (index < 3) {
                return 0;
            } else if (index < 6) {
                return mScreenWidth / 3;
            } else {
                return mScreenWidth / 3 * 2;
            }

        }
        return 0;

    }

}
