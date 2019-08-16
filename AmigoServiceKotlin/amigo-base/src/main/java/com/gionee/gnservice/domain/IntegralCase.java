package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.IntegralPrizeModel;
import com.gionee.gnservice.domain.model.IntegralRecordModel;
import com.gionee.gnservice.domain.model.IntegralTaskModel;
import com.gionee.gnservice.domain.model.IntegralTaskUploadModel;
import com.gionee.gnservice.domain.model.IntegralUserModel;
import com.gionee.gnservice.domain.model.LotteryTimesModel;
import com.gionee.gnservice.entity.IntegralInterface;
import com.gionee.gnservice.entity.IntegralRecord;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.entity.IntegralTaskResult;
import com.gionee.gnservice.utils.LogUtil;

import java.util.List;

/**
 * Created by caocong on 5/31/17.
 */
public class IntegralCase extends Case {
    private static final String TAG = IntegralCase.class.getSimpleName();
    private IntegralPrizeModel mIntegralPrizeModel;
    private IntegralTaskModel mIntegralTaskModel;
    private IntegralTaskUploadModel mIntegralTaskUploadModel;
    private IntegralRecordModel mIntegralRecordModel;
    private IntegralUserModel mIntegralUserModel;
    private LotteryTimesModel mLotteryTimesModel;

    public IntegralCase(IAppContext appContext) {
        super(appContext);

    }

    public void getIntegralAllPrize(Observer<List<String>> observer) {
        if (mIntegralPrizeModel == null) {
            mIntegralPrizeModel = new IntegralPrizeModel(mAppContext);
        }
        execute(mIntegralPrizeModel.getIntegralAllPrizes(), observer);
    }

    public void getIntegralMakePrizes(Observer<List<String>> observer) {
        if (mIntegralPrizeModel == null) {
            mIntegralPrizeModel = new IntegralPrizeModel(mAppContext);
        }
        execute(mIntegralPrizeModel.getIntegralMakePrizes(), observer);
    }

    public void getAllIntegralRecords(String token, int page, Observer<List<IntegralRecord>> observer) {
        getIntegralRecords(token, page, 0, observer);
    }

    public void getConsumeIntegralRecords(String token, int page, Observer<List<IntegralRecord>> observer) {
        getIntegralRecords(token, page, 2, observer);
    }

    public void getObtainIntegralRecords(String token, int page, Observer<List<IntegralRecord>> observer) {
        getIntegralRecords(token, page, 51, observer);
    }

    private void getIntegralRecords(String token, int page, int type, Observer<List<IntegralRecord>> observer) {
        mIntegralRecordModel = new IntegralRecordModel(mAppContext);
        LogUtil.d(TAG, "getIntegralRecords token is:" + token + ";page is:" + page + ";type is:" + type);
        execute(mIntegralRecordModel.getIntegralRecords(page, token, type), observer);
    }

    public void getIntegralTasks(String token, Observer<List<IntegralTask>> observer) {
        if (mIntegralTaskModel == null) {
            mIntegralTaskModel = new IntegralTaskModel(mAppContext);
        }
        execute(mIntegralTaskModel.getIntegralTasks(token), observer);
    }

    public void uploadIntegralTask(String token, int id, Observer<IntegralTaskResult> observer) {
        if (mIntegralTaskUploadModel == null) {
            mIntegralTaskUploadModel = new IntegralTaskUploadModel(mAppContext);
        }
        execute(mIntegralTaskUploadModel.uploadIntegralTask(id, token), observer);
    }

    public void getIntegralUser(String token, Observer<IntegralInterface> observer) {
        if (mIntegralUserModel == null) {
            mIntegralUserModel = new IntegralUserModel(mAppContext);
        }
        execute(mIntegralUserModel.getUserIntegral(token), observer);
    }

    public void getLotteryTimes(String token, Observer<Integer> observer) {
        if (mLotteryTimesModel == null) {
            mLotteryTimesModel = new LotteryTimesModel(mAppContext);
        }
        execute(mLotteryTimesModel.getLottyRemainTimes(token), observer);
    }
}
