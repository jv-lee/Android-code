package com.lee.code.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.lee.code.adapter.item.AItem;
import com.lee.code.adapter.item.BItem;
import com.lee.code.adapter.item.CItem;
import com.lee.code.adapter.item.DItem;
import com.lee.code.adapter.item.EItem;
import com.lee.code.bean.UserInfo;
import com.lee.library.adapter.LeeViewAdapter;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class MultiAdapter extends LeeViewAdapter<UserInfo> {

    public MultiAdapter(Context context, List<UserInfo> datas) {
        super(context,datas);
        addItemStyles(new AItem());
        addItemStyles(new BItem());
        addItemStyles(new CItem());
        addItemStyles(new DItem());
        addItemStyles(new EItem());
    }

}
