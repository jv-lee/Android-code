package com.gionee.gnservice.domain;

import android.graphics.Bitmap;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.domain.model.MemberCardLocalImgModel;
import com.gionee.gnservice.entity.MemberLevel;

import java.util.List;

/**
 * Created by caocong on 5/24/17.
 */
public class MemberCardImgCase extends Case {
    private MemberCardLocalImgModel mMemberCardImgModel;

    public MemberCardImgCase(IAppContext appContext) {
        super(appContext);
        mMemberCardImgModel = new MemberCardLocalImgModel(appContext);
    }

    public void getMemberCardImgs(MemberLevel level, Observer<List<Bitmap>> observer) {
        execute(mMemberCardImgModel.getMemberCardImg(level), observer);
    }

}
