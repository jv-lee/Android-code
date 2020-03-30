package com.lee.library.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author jv.lee
 * description：PageFragment适配器
 */
public class UiPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> tabList;
    private Fragment[] fragments;
    private String[] titles;

    public UiPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }

    public UiPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentList;
        this.tabList = tabList;
    }

    public UiPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }

    public UiPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
        this.titles = titles;
    }

    public void update(List<Fragment> fragmentList, List<String> tabList) {
        this.fragmentList = fragmentList;
        this.tabList = tabList;
        notifyDataSetChanged();
    }

    public void update(List<Fragment> fragmentList, String[] titles) {
        this.fragmentList = fragmentList;
        this.titles = titles;
        notifyDataSetChanged();
    }

    public void update(Fragment[] fragments, String[] titles) {
        this.fragments = fragments;
        this.titles = titles;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList == null ? fragments[position] : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? fragments.length : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position) {
            return titles[position];
        }
        if (tabList != null && tabList.size() > position) {
            return tabList.get(position);
        }
        return "";
    }
}