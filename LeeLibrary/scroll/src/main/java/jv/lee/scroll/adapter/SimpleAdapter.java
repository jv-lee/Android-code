package jv.lee.scroll.adapter;

import android.content.Context;

import com.lee.library.adapter.LeeViewAdapter;

import java.util.List;

/**
 * @author jv.lee
 * @date 2019-09-24
 * @description
 */
public class SimpleAdapter extends LeeViewAdapter<String> {

    public SimpleAdapter(Context context, List<String> data) {
        super(context, data);
        addItemStyles(new SimpleItem());
        addItemStyles(new SimpleListItem());
    }

}
