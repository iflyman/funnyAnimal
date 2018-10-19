package com.funnyAnimal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.funnyAnimal.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 青峰 on 2017/9/23.
 */

public class AboutUsActivity extends SwipeBackActivity {

    @BindView(R.id.red_dot)
    View redDot;

    @Override
    protected int getLayoutId() {
        return R.layout.act_aboutus;
    }

    @Override
    protected void afterViews() {

    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (redDot != null)
                        redDot.setVisibility(View.GONE);
                    break;
                case 1:
                    if (redDot != null)
                        redDot.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //用户反馈
        FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
            @Override
            public void onSuccess(int i) {
                if (i > 0) {
                    mHandler.sendEmptyMessage(1);
                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @OnClick({R.id.back_btn, R.id.version_des, R.id.feed_back})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.version_des:
                startActivity(new Intent(this, VersionDesActivity.class));
                break;
            case R.id.feed_back:
                FeedbackAPI.openFeedbackActivity();
                break;
            default:
                break;
        }
    }
}
