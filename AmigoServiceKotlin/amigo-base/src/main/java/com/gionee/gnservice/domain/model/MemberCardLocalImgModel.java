package com.gionee.gnservice.domain.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.gionee.gnservice.base.IAppContext;
import com.gionee.gnservice.entity.MemberLevel;
import com.gionee.gnservice.utils.ImageOOMUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caocong on 6/27/17.
 */
public class MemberCardLocalImgModel extends BaseModel {
    private static final String TAG = MemberCardLocalImgModel.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static String[] sGoldImgRes =
            {"uc_bg_member_card_view_unlogin_gold", "uc_bg_member_card_view_login_gold",
                    "uc_bg_member_card_view_login_gold_back"};
    private static String[] sPlatinumImgRes =
            {"uc_bg_member_card_view_unlogin_platinum", "uc_bg_member_card_view_login_platinum",
                    "uc_bg_member_card_view_login_platinum_back"};
    private static String[] sDiamondImgRes =
            {"uc_bg_member_card_view_unlogin_diamond", "uc_bg_member_card_view_login_diamond",
                    "uc_bg_member_card_view_login_diamond_back"};
    private static String[] sBlackGoldImgRes =
            {"uc_bg_member_card_view_unlogin_black_gold", "uc_bg_member_card_view_login_black_gold",
                    "uc_bg_member_card_view_login_black_gold_back"};

    public MemberCardLocalImgModel(IAppContext appContext) {
        super(appContext);
    }

    public com.gionee.gnservice.domain.Observable<List<Bitmap>> getMemberCardImg(
            final MemberLevel level) {
        return new com.gionee.gnservice.domain.Observable<List<Bitmap>>() {
            @Override
            protected Object doInBackground(Object... params) {
                String[] resArr = null;
                try {
                    switch (level) {
                        case PLATINUM:
                            resArr = sPlatinumImgRes;
                            break;
                        case DIAMOND:
                            resArr = sDiamondImgRes;
                            break;
                        case BLACK_GOLD:
                            resArr = sBlackGoldImgRes;
                            break;
                        case GOLD:
                        default:
                            resArr = sGoldImgRes;
                            break;

                    }
                    List<Bitmap> bitmaps = new ArrayList<Bitmap>();

                    Context context = mAppContext.application();

                    for (int i = 0; i < resArr.length; i++) {
                        Bitmap bitmap = ImageOOMUtil.getBitmap(
                                context, ResourceUtil.getDrawableId(context, resArr[i]));
                        bitmaps.add(bitmap);
                    }
                    publishNext(bitmaps);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(TAG, "load member card img error");
                    publishError(e);
                }
                return null;
            }
        };

    }

}
