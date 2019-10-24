package com.netease.project.presenter;

import com.netease.core.network.rx.databus.RegisterRxBus;
import com.netease.project.bean.Girl;
import com.netease.project.task.GirlTaskImpl;
import com.netease.project.task.IGirlTask;
import com.netease.project.view.IGirlView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public class GirlPresenter<T extends IGirlView> {
    private WeakReference<T> mView;

    private IGirlTask iGirlTask;

    public GirlPresenter(T view) {
        this.mView = new WeakReference<>(view);

        iGirlTask = new GirlTaskImpl();
        iGirlTask.loadGirlData();
    }

    @RegisterRxBus
    public void showGirlDataAction(ArrayList<Girl> girls) {
        mView.get().showGirlData(girls);
    }
}
