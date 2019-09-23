package jv.lee.scroll;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author jv.lee
 * SwipeRefreshLayout + HorizontalScrollView（子view） 滑动冲突解决
 * 外部拦截发， 通过修改父容器SwipeRefreshLayout 事件分发机制，传递方法来解决子view 的滑动事件冲突
 */
public class ScrollConflict1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_conflict1);

        SwipeRefreshLayout refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(() -> refresh.setRefreshing(false));
    }
}
