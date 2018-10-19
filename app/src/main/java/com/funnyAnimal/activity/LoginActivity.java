package com.funnyAnimal.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.funnyAnimal.R;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.UiHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by 青峰 on 2017/7/8.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.bt_login)
    Button btLogin;

    @BindView(R.id.fab_register)
    FloatingActionButton fabRegister;

    private MainApi mainApi;
    public static Activity instance;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_login;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
        instance = this;
    }

    @OnClick({R.id.bt_login, R.id.fab_register, R.id.union_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_register:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabRegister, fabRegister.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_login:
                ArrayMap<String, String> userLogin = new ArrayMap<>(7);
                String userPhone = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(password)) {
                    UiHelper.showToast(this, "请填写手机号或密码");
                    return;
                }
                userLogin.put("userPhone", etUsername.getText().toString());
                userLogin.put("password", etPassword.getText().toString());
                showProgressDialog("正在登陆");
                toUserLogin(userLogin);
                break;
            case R.id.union_qq:
            case R.id.tv_qq:
                UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
                break;
            default:
                break;
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showProgressDialog("正在授权登录");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (null == data) {
                UiHelper.showToast(LoginActivity.this, "授权失败，请重试");
                return;
            }
            ArrayMap<String, String> userInfo = new ArrayMap<>(7);
            userInfo.put("uid", data.get("uid"));
            userInfo.put("name", data.get("name"));
            userInfo.put("accessToken", data.get("access_token"));
            userInfo.put("gender", data.get("gender"));
            userInfo.put("iconurl", data.get("iconurl"));
            userInfo.put("city", data.get("city"));
            userInfo.put("province", data.get("province"));
            toLogin(userInfo);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissProgressDialog();
            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            dismissProgressDialog();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void toLogin(final ArrayMap<String, String> userInfo) {
        showProgressDialog("正在登陆");
        addSubscriptions(mainApi.qqLogin(userInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Log.e("bokey", e.getMessage());
                    }

                    @Override
                    public void onNext(Result<UserInfo> result) {
                        if (0 == result.code) {
                            UiHelper.saveUserInfo(result.data);
                            EventBus.getDefault().postSticky(result.data);
                            finish();
                        } else {
                            UiHelper.showToast(LoginActivity.this, result.message);
                        }
                    }
                }));
    }

    private void toUserLogin(ArrayMap<String, String> userLogin) {
        addSubscriptions(mainApi.userLogin(userLogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Log.e("bokey", e.getMessage());
                    }

                    @Override
                    public void onNext(Result<UserInfo> result) {
                        if (0 == result.code && null != result.data) {
                            UiHelper.saveUserInfo(result.data);
                            EventBus.getDefault().postSticky(result.data);
                            finish();
                        } else {
                            UiHelper.showToast(LoginActivity.this, result.message);
                        }
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        UMShareAPI.get(this).deleteOauth(LoginActivity.this, SHARE_MEDIA.QQ, null);
    }
}
