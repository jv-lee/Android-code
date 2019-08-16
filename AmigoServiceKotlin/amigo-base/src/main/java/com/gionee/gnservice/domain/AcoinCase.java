package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.ACoinBalanceModel;

/**
 * Created by caocong on 5/24/17.
 */
public class AcoinCase extends Case {
    private ACoinBalanceModel mACoinBalanceModel;

    public AcoinCase(IAppContext appContext) {
        super(appContext);
        mACoinBalanceModel = new ACoinBalanceModel(appContext);

    }

    public void getACoinBalance(String userId, Observer<String> observer) {
        execute(mACoinBalanceModel.getACoinBalance(userId), observer);
    }

}
