package com.gionee.gnservice.module.setting;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;

/**
 * Created by borney on 4/19/17.
 */

public interface IMemberSettingContract {

    interface Presenter extends IPresenter {
        void logout();
    }

    interface View extends IView {
        void startLogout();

        void logoutSuccess();

        void logoutError();
    }
}
