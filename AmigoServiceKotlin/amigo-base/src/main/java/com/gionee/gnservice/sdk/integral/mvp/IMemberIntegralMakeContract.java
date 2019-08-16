package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.entity.IntegralTaskResult;

import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class IMemberIntegralMakeContract {

    public interface View extends IView {

        void showLoadStartView();

        void showIntegralTaskView(List<IntegralTask> integralTasks);

        void showLoadFailView();

        void showPrizeView(List<String> prize);

        void showUploadIntegralTaskView(IntegralTaskResult result);

    }

    public interface Presenter extends IPresenter {

        void loadIntegralTasks();

        void loadPrize();

        void uploadIntegralTask(int id);

    }
}
