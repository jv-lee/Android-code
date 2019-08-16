package com.gionee.gnservice.module.bbs;

import android.view.View;
import com.gionee.gnservice.R;
import com.gionee.gnservice.base.webview.BaseTokenWebViewActivity;
import com.gionee.gnservice.config.AppConfig;
import com.gionee.gnservice.utils.RepeatClickUtil;

/**
 * Created by caocong on 1/9/17.
 */
public class AmigoBBSActivity extends BaseTokenWebViewActivity implements View.OnClickListener {
    private static final String TAG = AmigoBBSActivity.class.getSimpleName();

    @Override
    protected String getUrl() {
        return AppConfig.URL.getBbsUrl();
    }

    @Override
    protected String getActionbarTitle() {
        return getString(R.string.uc_recyleview_user_center_module_amigo_bbs);
    }

    @Override
    public void onClick(View v) {
        if (!RepeatClickUtil.canRepeatClick(v)) {
            return;
        }

        if (v.getId() == R.id.txt_webview_load_fail) {
            reLoadUrl();
        }
    }
}
