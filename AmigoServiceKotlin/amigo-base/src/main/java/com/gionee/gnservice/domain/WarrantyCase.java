package com.gionee.gnservice.domain;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.WarrantyModel;

/**
 * Created by caocong on 5/24/17.
 */
public class WarrantyCase extends Case {
    private WarrantyModel mWarrantyModel;

    public WarrantyCase(IAppContext appContext) {
        super(appContext);
        mWarrantyModel = new WarrantyModel(mAppContext);
    }

    public void getWarranty(String token, Observer<Integer> observer) {
        execute(mWarrantyModel.getWarranty(token), observer);
    }
}
