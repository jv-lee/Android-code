package com.gionee.gnservice.module.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.gionee.gnservice.R;
import com.gionee.gnservice.base.BaseRecyclerViewAdapter;
import com.gionee.gnservice.entity.ServiceInfo;

import java.util.List;

public class RecyclerViewServicesAdapter extends BaseRecyclerViewAdapter<ServiceInfo> {
    private static final String TAG = RecyclerViewServicesAdapter.class.getSimpleName();


    public RecyclerViewServicesAdapter(Context context, List<ServiceInfo> dataList) {
        super(context, dataList);
    }

    @Override
    public void convert(ViewHolder holder, ServiceInfo serviceInfo, int position) {
        TextView txt = holder.getView(R.id.txt_list_item_user_center_modules_text);
        txt.setText(serviceInfo.getName());
        ImageView img = holder.getView(R.id.img_list_item_user_center_modules_icon);
        img.setImageResource(serviceInfo.getIcon());
    }

    @Override
    public int getLayoutId() {
        return R.layout.uc_list_item_user_center_modules;
    }
}
