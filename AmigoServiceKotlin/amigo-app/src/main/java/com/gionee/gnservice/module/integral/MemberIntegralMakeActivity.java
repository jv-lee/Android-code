package com.gionee.gnservice.module.integral;

import amigoui.app.AmigoActionBar;
import amigoui.app.AmigoActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import com.gionee.gnservice.entity.IntegralTask;
import com.gionee.gnservice.entity.IntegralTaskResult;
import com.gionee.gnservice.sdk.integral.MemberIntegralRecordActivity;
import com.gionee.gnservice.sdk.integral.mvp.IMemberIntegralMakeContract;
import com.gionee.gnservice.sdk.integral.mvp.MemberIntegralMakePresenter;
import com.gionee.gnservice.statistics.StatisticsEvents;
import com.gionee.gnservice.statistics.StatisticsUtil;
import com.gionee.gnservice.utils.LogUtil;
import com.gionee.gnservice.utils.ResourceUtil;
import com.gionee.gnservice.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.gionee.gnservice.utils.ResourceUtil.*;

/**
 * Created by caocong on 1/9/17.
 */
public class MemberIntegralMakeActivity extends BaseAmigoActionBarActivity implements IMemberIntegralMakeContract.View {
    private static final String TAG = MemberIntegralMakeActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    private final Set<Class<? extends Fragment>> sShowed = new HashSet<Class<? extends Fragment>>();
    private AmigoActionBar mActionBar;
    private IMemberIntegralMakeContract.Presenter mPresenter;

    @Override
    protected void initVariables() {
        super.initVariables();
        mPresenter = new MemberIntegralMakePresenter(appContext(), this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        mViewPager = getView(getWidgetId(this, "viewpager_member_integral_make"));
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mActionBar.onScrollToEnd(v, event);
                return false;
            }
        });
        mActionBar = getAmigoActionBar();
        if (mActionBar != null) {
            mActionBar.setNavigationMode(AmigoActionBar.NAVIGATION_MODE_TABS);
            mTabsAdapter = new TabsAdapter(this, mViewPager);
            mTabsAdapter.addTab(mActionBar.newTab().setText(getString(getStringId(this, "uc_txt_member_integral_make_daily_task"))),
                    DailyTaskFragment.class, null);
            mTabsAdapter.addTab(mActionBar.newTab().setText(getString(getStringId(this, "uc_txt_member_integral_make_tiro_task"))),
                    TiroTaskFragment.class, null);
            mTabsAdapter.setTab(0);
        }

    }


    @Override
    protected String getActionbarRightTitle() {
        return ResourceUtil.getString(this, "uc_txt_member_integral_make_obtain_record");
    }

    @Override
    protected void onActionRightTextClick(View view) {
        super.onActionRightTextClick(view);
        Intent intent = new Intent(this, MemberIntegralRecordActivity.class);
        intent.putExtra("type", MemberIntegralRecordActivity.TYPE_OBTAIN_RECORD);
        startActivity(intent);
        StatisticsUtil.onEvent(this, StatisticsEvents.Main.GAIN_SCORE_RECORD);
    }

    public Map<Class<?>, Fragment> getFragmentMap() {
        return mTabsAdapter.fragments;
    }

    @Override
    protected String getActionbarTitle() {
        return getString(getStringId(this, "uc_title_member_integral_make_actionbar"));
    }

    @Override
    protected int getLayoutResId() {
        return getLayoutId(this, "uc_activity_member_integral_make");
    }

    @Override
    public void showLoadStartView() {

    }

    @Override
    public void showIntegralTaskView(List<IntegralTask> integralTasks) {
        DailyTaskFragment fragment = (DailyTaskFragment) mTabsAdapter.getItem(0);
        fragment.showIntegralTaskView(integralTasks);

        TiroTaskFragment fragment2 = (TiroTaskFragment) mTabsAdapter.getItem(1);
        fragment2.showIntegralTaskView(integralTasks);

        mPresenter.loadPrize();
    }

    @Override
    public void showLoadFailView() {
        ToastUtil.showLong(this, ResourceUtil.getString(this, "uc_network_exception"));
        DailyTaskFragment fragment = (DailyTaskFragment) mTabsAdapter.getItem(0);
        fragment.showLoadFailView();

        TiroTaskFragment fragment2 = (TiroTaskFragment) mTabsAdapter.getItem(1);
        fragment2.showLoadFailView();
    }

    @Override
    public void showPrizeView(List<String> prizes) {
        DailyTaskFragment fragment = (DailyTaskFragment) mTabsAdapter.getItem(0);
        fragment.showPrizeView(prizes);

        TiroTaskFragment fragment2 = (TiroTaskFragment) mTabsAdapter.getItem(1);
        fragment2.showPrizeView(prizes);
    }

    @Override
    public void showUploadIntegralTaskView(IntegralTaskResult result) {

    }

    class TabsAdapter extends FragmentPagerAdapter
            implements AmigoActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context context;
        private final AmigoActionBar actionBar;
        private final ViewPager viewPager;
        private final ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
        private final Map<Class<?>, Fragment> fragments = new HashMap<Class<?>, Fragment>();
        private boolean isFirstSelect = true;

        final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(AmigoActivity activity, ViewPager pager) {
            super(activity.getFragmentManager());
            context = activity;
            actionBar = activity.getAmigoActionBar();
            viewPager = pager;
            viewPager.setAdapter(this);
            viewPager.setOnPageChangeListener(this);
        }

        public void addTab(AmigoActionBar.Tab tab, Class<? extends Fragment> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            tabs.add(info);
            try {
                tab.setTextColor((ColorStateList) getResources().getColorStateList(ResourceUtil.getDrawableId(context, "uc_integral_coupon_selector")));
            } catch (Throwable t) {
            }
            actionBar.addTab(tab);
            actionBar.setIndicatorBackgroundColor(Color.parseColor("#d1773b"));
            Fragment fragment = getFragmentManager().findFragmentByTag(getFragmentTag(clss));
            if (fragment == null) {
                fragments.put(info.clss, Fragment.instantiate(context, info.clss.getName(), info.args));
            } else {
                fragments.put(info.clss, fragment);
            }
            notifyDataSetChanged();
        }

        private String getFragmentTag(Class<? extends Fragment> clazz) {
            return "service:switcher:" + clazz.getSimpleName();
        }

        public void setTab(int plan) {
            viewPager.setCurrentItem(plan);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = tabs.get(position);
            return fragments.get(info.clss);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            actionBar.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            actionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onTabSelected(AmigoActionBar.Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i = 0; i < tabs.size(); i++) {
                TabInfo info = tabs.get(i);
                if (info == tag) {
                    viewPager.setCurrentItem(i);
                    if (isFirstSelect) {
                        mPresenter.loadIntegralTasks();
                    }
                    isFirstSelect = false;
                }
            }
        }

        @Override
        public void onTabUnselected(AmigoActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(AmigoActionBar.Tab tab, FragmentTransaction ft) {
        }
    }

}
