package code.lee.code.design.adapter.item;

import com.lee.library.adapter.LeeViewHolder;
import com.lee.library.adapter.listener.LeeViewItem;

import code.lee.code.design.R;

/**
 * @author jv.lee
 * @date 2019/4/5
 */
public class DataItem implements LeeViewItem<String> {
    @Override
    public int getItemLayout() {
        return R.layout.itemfeed;
    }

    @Override
    public boolean openClick() {
        return false;
    }

    @Override
    public boolean isItemView(String entity, int position) {
        return true;
    }

    @Override
    public void convert(LeeViewHolder holder, String entity, int position) {

    }
}
