package com.funnyAnimal.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.funnyAnimal.BuildConfig;
import com.funnyAnimal.R;
import com.funnyAnimal.activity.AboutUsActivity;
import com.funnyAnimal.activity.DonateUsActicity;
import com.funnyAnimal.activity.LoginActivity;
import com.funnyAnimal.activity.MyApplication;
import com.funnyAnimal.activity.UserCenterActivity;
import com.funnyAnimal.activity.WrapperActivity;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/10/30.
 */

public class UserInfoFragment extends BaseFragment {
    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_motto)
    TextView userMotto;

    @BindView(R.id.wrapper_rl)
    RelativeLayout wrapperRl;

    @BindView(R.id.tv_modify)
    TextView tvModify;

    @BindView(R.id.red_dot)
    View redDot;

    @Override
    protected int getLayoutId() {
        return R.layout.act_user_info;
    }

    @Override
    protected void afterViews() {

    }

    @OnClick({R.id.wrapper_rl, R.id.user_rl, R.id.btn_exit, R.id.about_us, R.id.btn_donate, R.id.btn_focus})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.wrapper_rl:
                startActivity(new Intent(getContext(), WrapperActivity.class));
                break;
            case R.id.user_rl:
                if (TextUtils.isEmpty(PreferenceUtil.getUserUid())) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getContext(), UserCenterActivity.class));
                }
                break;
            case R.id.btn_exit:
                toExit();
                break;
            case R.id.about_us:
                redDot.setVisibility(View.GONE);
                startActivity(new Intent(getContext(), AboutUsActivity.class));
                break;
            case R.id.btn_donate:
                startActivity(new Intent(getContext(), DonateUsActicity.class));
                break;
            case R.id.btn_focus:
                //我的关注
                break;
            case R.id.btn_fans:
                //我的粉丝
                break;
            default:
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    redDot.setVisibility(View.GONE);
                    break;
                case 1:
                    redDot.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            checkFeedBack();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(PreferenceUtil.getUserUid())) {
            ImageLoadFactory.display2(getContext(), PreferenceUtil.getUserBitmapUrl().replace("https", "http"), userPhoto);
            userName.setText(PreferenceUtil.getUserName());
            userMotto.setText(PreferenceUtil.getUserMoto());
            String wapperId = PreferenceUtil.getWrapperResource();
            if (!TextUtils.isEmpty(wapperId)) {
                wrapperRl.setBackgroundResource(getResources().getIdentifier(wapperId,"mipmap", BuildConfig.APPLICATION_ID));
            }
            tvModify.setText("编辑");
        }
    }

    private void toExit() {
        new AlertDialog.Builder(getContext())
                .setTitle("温馨提示")
                .setMessage("确定要退出登陆并清除数据吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.accessToken = "";
                        PreferenceUtil.setUserUuid("");
                        tvModify.setText("登录");
                        userName.setText("火星人");
                        userMotto.setText("您还没有登录");
                        userPhoto.setImageResource(R.mipmap.ic_user_photo);
                        UiHelper.destroyUserInfo();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void checkFeedBack(){
        Log.e("bokey","检查反馈消息");
        FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
            @Override
            public void onSuccess(int i) {
                if(i > 0){
                    mHandler.sendEmptyMessage(1);
                }else {
                    mHandler.sendEmptyMessage(0);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }
}
