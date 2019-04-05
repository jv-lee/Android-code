package code.lee.code.design;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lee.library.base.BaseActivity;
import com.lee.library.ioc.annotation.ContentView;
import com.lee.library.ioc.annotation.InjectView;

import java.util.ArrayList;
import java.util.List;

import code.lee.code.design.adapter.SimpleAdapter;
import code.lee.code.design.widget.ImageViewRound;

/**
 * @author jv.lee
 */
@ContentView(R.layout.activity_recycler_view)
public class RecyclerViewActivity extends BaseActivity {

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.suspension_bar)
    RelativeLayout mSuspensionBar;
    @InjectView(R.id.tv_nickname)
    TextView mSuspensionTv;
    @InjectView(R.id.iv_avatar)
    ImageViewRound mSuspensionIv;

    int mSuspensionHeight;
    int mCurrentPosition;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void bindData(Bundle savedInstanceState) {

        statusBar();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SimpleAdapter(initData()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取悬浮条高度
                mSuspensionHeight = mSuspensionBar.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //对悬浮条的位置进行调整
                View view = recyclerView.getLayoutManager().findViewByPosition(mCurrentPosition + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        //需要对悬浮条进行移动
                        mSuspensionBar.setY(-(mSuspensionHeight - view.getTop()));
                    }else{
                        //保持在原来的位置
                        mSuspensionBar.setY(0);
                    }
                }
                if (mCurrentPosition != ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition()) {
                    mCurrentPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    updateSuspensionBar();
                }
            }
        });
        updateSuspensionBar();
    }

    private void statusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        //5.0以设置沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色调整
            window.setStatusBarColor(Color.TRANSPARENT);

            int visibility = window.getDecorView().getSystemUiVisibility();
            //布局内容全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //隐藏虚拟导航栏
//            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            //设置沉浸式 导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //防止内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(visibility);
            //4.0设置
        }else{
            //设置沉浸式 状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置沉浸式 导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        }
        setHeightAndPadding(this,findViewById(R.id.toolbar));
    }

    public int getStatusBarHeight(Context context) {
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    public void setHeightAndPadding(Context context, View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int statusHeight = getStatusBarHeight(context);
        layoutParams.height += statusHeight;
        view.setPadding(view.getPaddingLeft(),view.getPaddingTop()+statusHeight,view.getPaddingRight(),view.getPaddingBottom());
    }

    /**
     * 更新数据
     */
    private void updateSuspensionBar() {
        mSuspensionTv.setText("当前数据下标为：" + mCurrentPosition);
    }

    private List<String> datas;

    public List<String> initData(){
        datas = new ArrayList();
        for (int i = 0; i < 100; i++) {
            datas.add("index:" + i);
        }
        return datas;
    }
}
