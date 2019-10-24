package com.netease.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.project.R;
import com.netease.project.bean.Girl;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/10/22.
 * @description
 */
public class GirlAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Girl> girls;

    public GirlAdapter(LayoutInflater inflater, List<Girl> girls) {
        this.inflater = inflater;
        this.girls = girls;
    }

    @Override
    public int getCount() {
        return girls.size();
    }

    @Override
    public Object getItem(int position) {
        return girls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item, parent, false);
        Girl girl = girls.get(position);

        ImageView ivIcon = view.findViewById(R.id.iv_icon);
        ivIcon.setImageResource(R.mipmap.ic_launcher);
        TextView tvLink = view.findViewById(R.id.tv_link);
        tvLink.setText(girl.getLike());
        TextView tvStyle = view.findViewById(R.id.tv_style);
        tvStyle.setText(girl.getStyle());
        return view;
    }
}
