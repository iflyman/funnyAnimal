package com.funnyAnimal.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhyan on 2017/4/14.
 */

public class TabsAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void insertFragment(Fragment fragment, String title) {
        mFragments.add(0, fragment);
        mFragmentTitles.add(0, title);
    }

    public void clear(FragmentManager fm) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : mFragments) {
            transaction.remove(fragment);
        }
        transaction.commitAllowingStateLoss();
        mFragmentTitles.clear();
        mFragments.clear();
        fm.executePendingTransactions();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mFragmentTitles.isEmpty())
            return null;
        return mFragmentTitles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
