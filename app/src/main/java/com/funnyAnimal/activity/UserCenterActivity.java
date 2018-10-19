package com.funnyAnimal.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/6/29.
 */

public class UserCenterActivity extends SwipeBackActivity {

    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_gender)
    TextView tvGender;

    @BindView(R.id.tv_area)
    TextView tvArea;

    @BindView(R.id.tv_moto)
    TextView tvMoto;

    @Override
    protected int getLayoutId() {
        return R.layout.act_user_center;
    }

    @Override
    protected void afterViews() {}

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(PreferenceUtil.getUserBitmapUrl())){
            ImageLoadFactory.display2(this, PreferenceUtil.getUserBitmapUrl().replace("https","http"), userPhoto);
        }
        tvName.setText(PreferenceUtil.getUserName());
        tvGender.setText(PreferenceUtil.getUserGender());
        tvArea.setText(PreferenceUtil.getUserProvince()+"-"+PreferenceUtil.getUserCity());
        tvMoto.setText(PreferenceUtil.getUserMoto());
    }

    @OnClick({R.id.back_btn,R.id.btn_modify})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_modify:
                startActivity(new Intent(UserCenterActivity.this,ModifyUserInfoActivity.class));
                break;
            default:
                break;
        }
    }
}
