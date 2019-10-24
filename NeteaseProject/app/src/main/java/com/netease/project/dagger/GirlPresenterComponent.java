package com.netease.project.dagger;

import com.netease.project.MainActivity;

import dagger.Component;

/**
 * @author jv.lee
 * @date 2019/10/24.
 * @description
 */
@Component(modules = GirlPresenterModule.class)
public interface GirlPresenterComponent {

    void inject(MainActivity mainActivity);

}
