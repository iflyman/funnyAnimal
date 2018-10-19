package com.funnyAnimal.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnyAnimal.BuildConfig;
import com.funnyAnimal.R;
import com.funnyAnimal.activity.AboutUsActivity;
import com.funnyAnimal.activity.DonateUsActicity;
import com.funnyAnimal.activity.LoginActivity;
import com.funnyAnimal.activity.MainActivity;
import com.funnyAnimal.activity.MyApplication;
import com.funnyAnimal.activity.UserCenterActivity;
import com.funnyAnimal.activity.WrapperActivity;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/7/27.
 */

public class MenuLeftFragment extends BaseFragment {

    @BindView(R.id.tv_modify)
    TextView tvModify;

    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.tv_moto)
    TextView tvMoto;

    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.user_info_rl)
    RelativeLayout userInfoRl;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_menu;
    }

    @Override
    protected void afterViews() {
        if (TextUtils.isEmpty(MyApplication.accessToken)) {
            tvModify.setText("登录");
        } else {
            tvModify.setText("编辑");
        }
        if(!TextUtils.isEmpty(PreferenceUtil.getUserBitmapUrl())){
            ImageLoadFactory.display2(getContext(), PreferenceUtil.getUserBitmapUrl().replace("https","http"), userPhoto);
        }
        tvName.setText(PreferenceUtil.getUserName());
        tvMoto.setText(PreferenceUtil.getUserMoto());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(MyApplication.accessToken)) {
            tvModify.setText("登录");
        } else {
            tvModify.setText("编辑");
        }
        if(!TextUtils.isEmpty(PreferenceUtil.getUserBitmapUrl())){
            ImageLoadFactory.display2(getContext(), PreferenceUtil.getUserBitmapUrl().replace("https","http"), userPhoto);
        }
        tvName.setText(PreferenceUtil.getUserName());
        tvMoto.setText(PreferenceUtil.getUserMoto());
        String wapperId = PreferenceUtil.getWrapperResource();
        if(!TextUtils.isEmpty(wapperId)){
            userInfoRl.setBackgroundResource(getResources().getIdentifier(wapperId,"mipmap", BuildConfig.APPLICATION_ID));
        }
    }

    @OnClick({R.id.user_info_rl, R.id.btn_exit,R.id.forum_btn,R.id.user_rl,R.id.btn_aboutus,R.id.btn_donate})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info_rl:
                startActivity(new Intent(getContext(), WrapperActivity.class));
                break;
            case R.id.user_rl:
                if (TextUtils.isEmpty(MyApplication.accessToken)) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getContext(), UserCenterActivity.class));
                }
                break;
            case R.id.btn_exit:
                toExit();
                break;
            case R.id.forum_btn:
//                startActivity(new Intent(getContext(),ForumHomeActivity.class));
                break;
            case R.id.btn_aboutus:
                startActivity(new Intent(getContext(),AboutUsActivity.class));
                break;
            case R.id.btn_donate:
                startActivity(new Intent(getContext(),DonateUsActicity.class));
                break;
            default:
                break;
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        tvModify.setText("编辑");
        if (!TextUtils.isEmpty(userInfo.iconurl)) {
            ImageLoadFactory.display2(getContext(), userInfo.iconurl.replace("https", "http"), userPhoto);
        }
        tvName.setText(userInfo.name);
        tvMoto.setText(userInfo.moto);
    }

    private void toExit(){
        new AlertDialog.Builder(getContext())
                .setTitle("温馨提示")
                .setMessage("确定要退出吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.accessToken = "";
                        PreferenceUtil.setUserUuid("");
                        tvModify.setText("登录");
                        tvName.setText("火星人");
                        tvMoto.setText("您还没有登录");
                        userPhoto.setImageResource(R.mipmap.ic_user_photo);
                        UiHelper.destroyUserInfo();
                        MainActivity mainActivity = (MainActivity)getActivity();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
