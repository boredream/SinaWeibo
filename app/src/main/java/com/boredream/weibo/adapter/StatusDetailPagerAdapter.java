package com.boredream.weibo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.fragment.StatusCommentListFragment;

public class StatusDetailPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabs;
    private final Goods status;

    public StatusDetailPagerAdapter(FragmentManager fm, String[] tabs, Goods status) {
        super(fm);
        this.tabs = tabs;
        this.status = status;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return StatusCommentListFragment.newInstance(status);
        } else if(position == 1) {
            return StatusCommentListFragment.newInstance(status);
        } else {
            return StatusCommentListFragment.newInstance(status);
        }
    }
}
