package com.funnyAnimal.activity;

import android.didikee.donate.AlipayDonate;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.funnyAnimal.R;
import com.funnyAnimal.utils.UiHelper;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by 青峰 on 2017/9/23.
 */

public class DonateUsActicity extends SwipeBackActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.act_donate_us;
    }

    @Override
    protected void afterViews() {

    }

    @OnClick({R.id.back_btn, R.id.btn_donate})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_donate:
                boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
                if (hasInstalledAlipayClient) {
                    AlipayDonate.startAlipayClient(this, "FKX05032IRN4RCHTYCQX2C");
                } else {
                    UiHelper.showToast(DonateUsActicity.this, "小萌趣无法跳转支付宝");
                }
                break;
            default:
                break;
        }
    }
}
