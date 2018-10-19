package com.funnyAnimal.login;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.funnyAnimal.R;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.AccountManager;
import com.funnyAnimal.utils.UiHelper;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bhyan on 2018/6/11.
 */

public class LoginActivity extends BaseActivity {

    private MainApi mainApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
    }

    @OnClick(R.id.btn_login)
    void btnLogin() {
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
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

            UiHelper.saveUserInfo(data);

            toLogin(userInfo);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            dismissProgressDialog();
            UiHelper.showToast(LoginActivity.this, "失败：" + t.getMessage());
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
        Log.e("bokey", userInfo.toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, null);
    }
}
