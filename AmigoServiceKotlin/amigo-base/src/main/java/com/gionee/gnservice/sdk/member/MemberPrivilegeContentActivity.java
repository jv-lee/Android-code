package com.gionee.gnservice.sdk.member;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gionee.gnservice.base.BaseSdkActivity;
import com.gionee.gnservice.common.imageloader.IImageLoader;
import com.gionee.gnservice.common.imageloader.ImageConfig;
import com.gionee.gnservice.common.imageloader.ImageLoaderImpl;
import com.gionee.gnservice.entity.MemberPrivilege;
import com.gionee.gnservice.entity.MemberPrivilegeContent;
import com.gionee.gnservice.utils.ChameleonColorManager;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;

import java.util.List;


public class MemberPrivilegeContentActivity extends BaseSdkActivity {
    private static final String TAG = MemberPrivilegeContentActivity.class.getSimpleName();
    private ImageView mImgBigPicture;
    private MemberPrivilege mMemberPrivilege;
    private ViewGroup mLayoutPrivilegeContent;


    @Override
    protected void initVariables() {
        Intent intent = getIntent();
        mMemberPrivilege = (MemberPrivilege) intent.getSerializableExtra(MemberPrivilegeActivity.KEY_INTENT_PRIVILEGE);
        if (mMemberPrivilege == null) {
            LogUtil.d(TAG, "initVariables mMemberPrivilege = null");
            return;
        }
        LogUtil.d(TAG, "content is :" + mMemberPrivilege.getContentParts());
    }

    @Override
    protected void initView() {
        mImgBigPicture = getView(ResourceUtil.getWidgetId(this, "img_member_privilege_content_big_pic"));
        mLayoutPrivilegeContent = getView(ResourceUtil.getWidgetId(this, "llayout_member_privilege_contents"));
        initContentPartsView();
        addChameleonColorView();
    }

    public void addChameleonColorView() {
        if (ChameleonColorManager.isPowerSavingMode()) {
            getView(ResourceUtil.getWidgetId(this, "layout_member_privilege_content")).setBackgroundColor(
                    ChameleonColorManager.getBackgroudColor_B1());
        }
    }

    private void initContentPartsView() {
        if (mMemberPrivilege == null) {
            return;
        }
        List<MemberPrivilegeContent> contents = mMemberPrivilege.getContentParts();
        if (contents == null || contents.isEmpty()) {
            LogUtil.d(TAG, "initContentPartsView isEmpty");
            return;
        }
        for (MemberPrivilegeContent mpc : contents) {
            mLayoutPrivilegeContent.addView(createContentPartView(mpc.getName(), mpc.getContent()));
        }
    }

    private View createContentPartView(String title, String content) {
        LogUtil.d("privilege title=" + title + "value=" + content);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(ResourceUtil.getLayoutId(this, "uc_list_item_member_privilege_content"), null);
        if (view == null) {
            return null;
        }
        TextView tvTitle = (TextView) view.findViewById(ResourceUtil.getWidgetId(this, "txt_list_item_member_privilege_content_title"));
        TextView tvContent = (TextView) view.findViewById(ResourceUtil.getWidgetId(this, "txt_list_item_member_privilege_content_content"));
        tvTitle.setText(title);
        tvContent.setText(content);
        return view;
    }

    @Override
    protected void initData() {
        loadBitmaps();

    }

    private void loadBitmaps() {
        if (mMemberPrivilege == null) {
            return;
        }
        IImageLoader imageLoader = ImageLoaderImpl.create(this);
        if (!TextUtils.isEmpty(mMemberPrivilege.getImgUrl())) {
            LogUtil.d(TAG, "imgUrl is:" + mMemberPrivilege.getImgUrl());
            ImageConfig.Builder builder1 = new ImageConfig.Builder();
            builder1.setUrl(mMemberPrivilege.getImgUrl())
                    .setImageView(mImgBigPicture)
                    .setCacheInDisk(true)
                    .setCacheInMemory(true)
                    .setErrorPic(ResourceUtil.getDrawableId(this, "uc_bg_member_privilege_content_big_pic_loading"))
                    .setPlaceholder(ResourceUtil.getDrawableId(this, "uc_bg_member_privilege_content_big_pic_loading"));
            imageLoader.loadImage(builder1.build());

        }
    }

    @Override
    protected String getActionbarTitle() {
        return mMemberPrivilege == null ? "" : mMemberPrivilege.getName();
    }


    @Override
    protected int getLayoutResId() {
        return ResourceUtil.getLayoutId(this, "uc_activity_member_privilege_content");
    }
}
