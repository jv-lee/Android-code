package com.gionee.gnservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.gionee.gnservice.module.main.PermissionHandler;
import com.gionee.gnservice.sdk.AmigoServiceCardView;
import com.gionee.gnservice.sdk.AmigoServiceSdk;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.SdkUtil;

import static com.gionee.gnservice.utils.ResourceUtil.getLayoutId;
import static com.gionee.gnservice.utils.ResourceUtil.getWidgetId;


public class SdkDemoActivity extends Activity {
    private PermissionHandler mPermissionHandler;

    private String txt = "以下是针对支持夜间模式的app,如浏览器";

    private String nightmode = "设置为夜间模式(此时不支持省电模式)";

    private String notSupportPowerMode = "不支持省电模式";

    private AmigoServiceCardView amigoServiceCardView;

    private CheckBox cbNightMode, cbPowerMode;

    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ResourceUtil.getStyleId(this, "AmigoServiceLightTheme"));
        SdkUtil.setFromSdkDemo(true);
        setContentView(getLayoutId(this, "uc_demo_main"));
        amigoServiceCardView = (AmigoServiceCardView) findViewById(getWidgetId(this, "uc_member_card_view"));

        mPermissionHandler = new PermissionHandler(this);
        mPermissionHandler.reqeustPermissions();

        cbNightMode = (CheckBox) findViewById(getWidgetId(this, "cb_night_mode"));
        cbNightMode.setText(nightmode);
        cbNightMode.setChecked(false);
        cbPowerMode = (CheckBox) findViewById(getWidgetId(this, "cb_power_mode"));
        cbPowerMode.setText(notSupportPowerMode);
        cbPowerMode.setChecked(true);
        cbPowerMode.setClickable(false);
        cbPowerMode.setVisibility(View.GONE);
        txtInfo = (TextView) findViewById(getWidgetId(this, "txt_info"));
        txtInfo.setText(txt);

        cbNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AmigoServiceSdk.getInstance().setNightMode(isChecked);
                AmigoServiceSdk.getInstance().setSupportPowerMode(!isChecked);
            }
        });

//        cbPowerMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                AmigoServiceSdk.getInstance().setSupportPowerMode(isChecked);
//            }
//        });
        changeColor();
    }

    private void changeColor() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AmigoServiceSdk.getInstance().onResume(amigoServiceCardView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
