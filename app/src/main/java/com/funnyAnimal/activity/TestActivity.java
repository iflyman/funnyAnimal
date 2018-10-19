package com.funnyAnimal.activity;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.funnyAnimal.R;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.utils.UiHelper;

import butterknife.BindView;

/**
 * Created by bhyan on 2017/6/21.
 */

public class TestActivity extends BaseActivity {

    @BindView(R.id.test_lin)
    LinearLayout linearLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.test_layout;
    }

    @Override
    protected void afterViews() {
        for (int i = 0;i<15;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(R.color.colorPrimary);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UiHelper.dp2px(this,50), UiHelper.dp2px(this,50));
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.topMargin = UiHelper.dp2px(this,50);
            imageView.setLayoutParams(params);
            linearLayout.addView(imageView);
        }
    }
}
