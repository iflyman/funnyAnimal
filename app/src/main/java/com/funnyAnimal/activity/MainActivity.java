package com.funnyAnimal.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.funnyAnimal.R;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.fragment.ForumFragment;
import com.funnyAnimal.fragment.HomeFragment;
import com.funnyAnimal.fragment.UserInfoFragment;
import com.hjm.bottomtabbar.BottomTabBar;

import butterknife.BindView;

/**
 * Created by bhyan on 2017/10/23.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.bottom_tab_bar)
    BottomTabBar tabBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterViews() {
        tabBar.init(getSupportFragmentManager())
                .addTabItem("首页", R.mipmap.tab_home_press, R.mipmap.tab_home, HomeFragment.class)
                .addTabItem("社区", R.mipmap.icon_forum_press, R.mipmap.icon_forum, ForumFragment.class)
                .addTabItem("我的", R.mipmap.tab_me_press, R.mipmap.tab_me, UserInfoFragment.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("温馨提示")
                    .setMessage("确定要退出吗?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return false;
    }
}
