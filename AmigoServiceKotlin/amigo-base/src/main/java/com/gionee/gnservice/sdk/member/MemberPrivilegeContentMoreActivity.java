package com.gionee.gnservice.sdk.member;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.entity.MemberPrivilegeContent;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;

import java.util.List;

public class MemberPrivilegeContentMoreActivity extends BaseSdkActivity {
    private static final String TAG = MemberPrivilegeContentMoreActivity.class.getSimpleName();
    private ImageView mImgTitleIcon;
    private MemberPrivilege mMemberPrivilege;

    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        mMemberPrivilege = (MemberPrivilege) intent.getSerializableExtra(MemberPrivilegeActivity.KEY_INTENT_PRIVILEGE);
        if (mMemberPrivilege == null) {
            LogUtil.d(TAG, "initVariables mMemberPrivilege == null");
            return;
        }
        LogUtil.d(TAG, "content is :" + mMemberPrivilege.getContentParts());
    }

    @Override
    protected void initView() {
        mImgTitleIcon = getView(ResourceUtil.getWidgetId(this, "img_member_privilege_content_more_icon"));
        if (mMemberPrivilege == null) {
            return;
        }
        List<MemberPrivilegeContent> contents = mMemberPrivilege.getContentParts();
        if (contents == null || contents.isEmpty()) {
            LogUtil.d(TAG, "initView isEmpty");
            return;
        }

        MemberPrivilegeContent content = contents.get(0);
        TextView txtTitle = getView(ResourceUtil.getWidgetId(this, "txt_member_privilege_content_more_title"));
        txtTitle.setText(content.getName());
        TextView txtContent = getView(ResourceUtil.getWidgetId(this, "txt_member_privilege_content_more_content"));
        txtContent.setText(content.getContent());
    }

    protected void addChameleonColorView() {
        /*if (ChameleonColorManager.isPowerSavingMode()) {
            getView(ResourceUtil.getWidgetId(this, "layout_member_privilege_content_more")).setBackgroundColor(
                    ChameleonColorManager.getBackgroudColor_B1());
        }*/
    }

    @Override
    protected int getLayoutResId() {
        return ResourceUtil.getLayoutId(this, "uc_activity_member_privilege_content_more");
    }

    @Override
    protected String getActionbarTitle() {
        return mMemberPrivilege == null ? "" : mMemberPrivilege.getName();
    }

}
