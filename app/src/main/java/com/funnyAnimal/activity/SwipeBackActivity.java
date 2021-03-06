package com.funnyAnimal.activity;

import android.os.Bundle;
import android.view.View;

import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.view.swipeback.SwipeBackDao;
import com.funnyAnimal.view.swipeback.SwipeBackHelper;
import com.funnyAnimal.view.swipeback.SwipeBackLayout;
import com.funnyAnimal.view.swipeback.Utils;

/**
 * Created by 青峰 on 2017/8/26.
 */

public abstract class SwipeBackActivity extends BaseActivity implements SwipeBackDao {
    private SwipeBackHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackHelper(this);
        mHelper.onActivityCreate();
        //实现第一种返回方式，这样就OK了
        //随手势返回的处罚方位，有EDGE_LEFT、EDGE_RIGHT、EDGE_BOTTOM、EDGE_ALL
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);

        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
