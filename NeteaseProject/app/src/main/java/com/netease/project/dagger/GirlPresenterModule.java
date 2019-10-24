package com.netease.project.dagger;

import com.netease.project.MainActivity;
import com.netease.project.presenter.GirlPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author jv.lee
 * @date 2019/10/24.
 * @description
 */
@Module
public class GirlPresenterModule {

    private MainActivity mView;

    public GirlPresenterModule(MainActivity mView) {
        this.mView = mView;
    }

    @Provides
    public GirlPresenter providesGirlPresenter() {
        return new GirlPresenter(mView);
    }
}
