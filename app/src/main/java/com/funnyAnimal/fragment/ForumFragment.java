package com.funnyAnimal.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.TabsAdapter;
import com.funnyAnimal.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by bhyan on 2017/10/24.
 */

public class ForumFragment extends BaseFragment {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TabsAdapter tabsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_forum;
    }

    @Override
    protected void afterViews() {
        if (null == tabsAdapter) {
            tabsAdapter = new TabsAdapter(getChildFragmentManager());
        }
        tabsAdapter.addFragment(new ForumInfoFragment(), "Attention");
        tabsAdapter.addFragment(new ForumInfoFragment(), "Video");
        tabsAdapter.addFragment(new ForumInfoFragment(), "Music");
        tabsAdapter.addFragment(new ForumInfoFragment(), "Photo");
        tabsAdapter.addFragment(new ForumInfoFragment(), "Photo&Article");
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).select();
        viewPager.setOffscreenPageLimit(3);
    }

}
