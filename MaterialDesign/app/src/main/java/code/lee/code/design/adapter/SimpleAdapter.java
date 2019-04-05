package code.lee.code.design.adapter;

import com.lee.library.adapter.LeeViewAdapter;

import java.util.ArrayList;
import java.util.List;

import code.lee.code.design.adapter.item.DataItem;

/**
 * @author jv.lee
 * @date 2019/4/5
 */
public class SimpleAdapter extends LeeViewAdapter {



    public SimpleAdapter(List datas) {
        super(datas);
        addItemStyles(new DataItem());
    }
}
