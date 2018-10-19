package com.funnyAnimal.user;

import android.app.ActivityOptions;
import android.content.Intent;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.login.LoginActivity;
import com.funnyAnimal.utils.AccountManager;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2018/6/11.
 */

public class UserLoginActivity extends BaseActivity {

    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.toLogin)
    TextView toLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.user_login;
    }

    @Override
    protected void afterViews() {

    }

    @Override
    protected void onResume() {
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

    @OnClick({R.id.toLogin, R.id.btn_return, R.id.btn_donate, R.id.btn_feedback, R.id.btn_about_us})
    void toClick(View view) {
        switch (view.getId()) {
            case R.id.toLogin:
                if (TextUtils.isEmpty(AccountManager.getName())) {
                    toLogin.setText("登录");
                    startActivity(new Intent(this, LoginActivity.class),
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                    getWindow().setEnterTransition(new Fade().setDuration(Constant.Duration));
                } else {
                    toLogin.setText("退出");
                    UiHelper.cleanUserInfo();
                }
                break;
            case R.id.btn_return:
                finish();
                break;
            case R.id.btn_donate:
                break;
            case R.id.btn_feedback:
                break;
            case R.id.btn_about_us:
                break;
            default:
                break;
        }
    }
}
