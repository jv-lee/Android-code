package com.gionee.gnservice.sdk.integral.mvp;

import com.gionee.gnservice.base.IPresenter;
import com.gionee.gnservice.base.IView;
import com.gionee.gnservice.entity.IntegralRecord;

import java.util.List;

/**
 * Created by caocong on 5/4/17.
 */

public class IMemberIntegralRecordContract {

    public interface View extends IView {

        void showIntegralRecordView(List<IntegralRecord> integralRecords);

        void showLoadFailView();

        void showIntegralUserView(int value);

    }

    public interface Presenter extends IPresenter {

        void loadIntegralRecords(int page);

        void loadObtainRecords(int page);

        void loadIntegralUser();

    }
}
