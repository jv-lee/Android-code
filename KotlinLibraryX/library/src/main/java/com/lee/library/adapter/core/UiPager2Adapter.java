package com.lee.library.adapter.core;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * @author jv.lee
 * description：PageFragment适配器
 */
public class UiPager2Adapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;
    private List<String> tabList;

    public UiPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> fragments, List<String> titles) {
        super(fragmentActivity);
        this.fragmentList = fragments;
        this.tabList = titles;
    }

    public UiPager2Adapter(@NonNull Fragment fragment, List<Fragment> fragments, List<String> titles) {
        super(fragment);
        this.fragmentList = fragments;
        this.tabList = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public List<String> getTabList() {
        return tabList;
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }
}