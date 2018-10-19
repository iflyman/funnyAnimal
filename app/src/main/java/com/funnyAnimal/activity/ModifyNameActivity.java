package com.funnyAnimal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;

import com.funnyAnimal.R;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bhyan on 2017/8/17.
 */

public class ModifyNameActivity extends SwipeBackActivity {

    @BindView(R.id.clear_edit)
    ClearEditText clearEdit;

    private MainApi mainApi;
    private String type;

    @Override
    protected int getLayoutId() {
        return R.layout.act_modify_name;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
        type = getIntent().getStringExtra("type");
        if (TextUtils.equals(type, "name")) {
            clearEdit.setHint(PreferenceUtil.getUserName());
        } else if (TextUtils.equals(type,"moto")) {
            clearEdit.setHint(PreferenceUtil.getUserMoto());
        }
    }

    @OnClick({R.id.back_btn, R.id.btn_commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_commit:
                toModifyName();
                break;
            default:
                break;
        }
    }

    private void toModifyName() {
        if (TextUtils.isEmpty(clearEdit.getText().toString())) {
            UiHelper.showToast(this, "您还没有进行任何修改哦~");
        }
        showProgressDialog("请稍等...");
        ArrayMap<String, String> params = new ArrayMap<>(1);
        String name = clearEdit.getText().toString();
        params.put(type, name);
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
                            if (TextUtils.equals(type, "name")) {
                                PreferenceUtil.setUserName(result.data.name);
                            } else if (TextUtils.equals(type, "moto")) {
                                PreferenceUtil.setUserMoto(result.data.moto);
                            }
                            Intent intent = new Intent(ModifyNameActivity.this, ModifyUserInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", type);
                            bundle.putString("data", clearEdit.getText().toString());
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            UiHelper.showToast(ModifyNameActivity.this, result.message);
                        }
                    }
                }));
    }
}
