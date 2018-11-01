package com.funnyAnimal.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.funnyAnimal.R;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.login.LoginActivity;
import com.funnyAnimal.utils.AccountManager;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/10/30.
 */

public class UserInfoFragment extends BaseFragment {
    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.toLogin)
    TextView toLogin;

    @BindView(R.id.red_dot)
    View redDot;

    @Override
    protected int getLayoutId() {
        return R.layout.user_login;
    }

    @Override
    protected void afterViews() {

    }

    @Override
    public void onResume() {
        super.onResume();
        judgeLogin();
    }

    private void judgeLogin() {
        if (TextUtils.isEmpty(AccountManager.getName())) {
            toLogin.setText("登录");
            tvName.setText("欢迎");
            userPhoto.setVisibility(View.GONE);
        } else {
            toLogin.setText("退出");
            tvName.setText(AccountManager.getName());
            userPhoto.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(AccountManager.getIconurl(), userPhoto);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            checkFeedBack();
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


    @OnClick({R.id.toLogin, R.id.btn_return, R.id.btn_donate, R.id.btn_about_us})
    void toClick(View view) {
        switch (view.getId()) {
            case R.id.toLogin:
                if (TextUtils.isEmpty(AccountManager.getName())) {
                    toLogin.setText("登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    getActivity().getWindow().setEnterTransition(new Fade().setDuration(Constant.Duration));
                } else {
                    toLogin.setText("退出");
                    UiHelper.cleanUserInfo();
                }
                break;
            case R.id.btn_return:
//                UiHelper.cleanUserInfo();
                break;
            case R.id.btn_donate:
//                startActivity(new Intent(getContext(), DonateUsActicity.class));
                break;
            case R.id.btn_about_us:
                redDot.setVisibility(View.GONE);
//                startActivity(new Intent(getContext(), AboutUsActivity.class));
                break;
            default:
                break;
        }
    }
}
