package com.lee.code.adapter.adapter;

import com.lee.code.adapter.adapter.item.*;
import com.lee.code.adapter.bean.Userinfo;
import com.lee.library.base.LeeViewAdapter;
import com.lee.library.listener.LeeViewItem;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019/3/29
 */
public class MultiAdapter extends LeeViewAdapter<Userinfo> {

    public MultiAdapter(List<Userinfo> datas) {
        super(datas);
        addItemStyles(new AItem());
        addItemStyles(new BItem());
        addItemStyles(new CItem());
        addItemStyles(new DItem());
        addItemStyles(new EItem());
    }

}
