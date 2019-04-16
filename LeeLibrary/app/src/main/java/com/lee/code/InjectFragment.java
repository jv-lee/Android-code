package com.lee.code;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.library.base.BaseFragment;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;
import com.lee.library.ioc.annotation.OnClick;

@ContentView(R.layout.fragment_inject)
public class InjectFragment extends BaseFragment {

    @InjectView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void bindData(Bundle savedInstanceState) {
        tvContent.setText("this is content text");
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick(values = {R.id.tv_content})
    public void onClick(View view) {
        Toast.makeText(mActivity, "click", Toast.LENGTH_SHORT).show();
    }

}
