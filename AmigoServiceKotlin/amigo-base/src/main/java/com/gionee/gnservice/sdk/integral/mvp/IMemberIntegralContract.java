package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;

import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class IMemberIntegralContract {

    public interface View extends IView {

        void showLotteryRemainTimeView(int times);

        void showIntegralMakeTasksNumber(int taskSize, int integralCanGet);

        void showIntegralPrizeView(List<String> prizeList);

        void showIntegralUserView(int value);

    }

    public interface Presenter extends IPresenter {
        void loadLotteryRemainTimes(String token);

        void loadIntegralMakeTasksNumber(String token);

        void loadIntegralPrizes();

        void loadIntegralUser(String token);
    }
}
