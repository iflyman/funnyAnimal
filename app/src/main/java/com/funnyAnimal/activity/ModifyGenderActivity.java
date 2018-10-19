package com.funnyAnimal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.funnyAnimal.R;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;

import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bhyan on 2017/8/24.
 */

public class ModifyGenderActivity extends SwipeBackActivity {

    private MainApi mainApi;

    @Override
    protected int getLayoutId() {
        return R.layout.act_modify_gender;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
    }

    @OnClick({R.id.back_btn, R.id.male, R.id.female})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.male:
                toCommit("男");
                break;
            case R.id.female:
                toCommit("女");
                break;
            default:
                break;
        }
    }

    private void toCommit(final String gender) {
        showProgressDialog("请稍等...");
        ArrayMap<String, String> params = new ArrayMap<>(2);
        params.put("gender", gender);
        params.put("accessToken",PreferenceUtil.getUserUuid());
        addSubscriptions(mainApi.userModify(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(Result<UserInfo> result) {
                        if (result.code == 0) {
                            PreferenceUtil.setUserGender(gender);
                            Intent intent = new Intent(ModifyGenderActivity.this, ModifyUserInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("gender", gender);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            UiHelper.showToast(ModifyGenderActivity.this, result.message);
                        }
                    }
                }));
    }
}
