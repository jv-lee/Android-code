package com.netease.project.presenter;

import com.netease.project.task.GirlTaskImpl;
import com.netease.project.task.IGirlTask;

import java.lang.ref.WeakReference;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public class GirlPresenter<T> {
    private WeakReference<T> mView;

    private IGirlTask iGirlTask;

    public GirlPresenter(T view){
        this.mView = new WeakReference<>(view);

        iGirlTask = new GirlTaskImpl();
        iGirlTask.loadGrilData();
    }
}
