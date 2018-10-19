package com.funnyAnimal.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.api.HttpTokenExpireFunc;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.QiniuErro;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;
import com.funnyAnimal.view.MultiSwipeRefreshLayout;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by 青峰 on 2017/7/8.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.mSwipeRefresh)
    MultiSwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.cv_add)
    CardView cvAdd;

    @BindView(R.id.radio_male)
    RadioButton radioMale;

    @BindView(R.id.radio_female)
    RadioButton radioFemale;

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R.id.tv_male)
    TextView tvMale;

    @BindView(R.id.tv_female)
    TextView tvFemale;

    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.et_repeat)
    EditText etRepeat;

    @BindView(R.id.et_userphone)
    EditText etUserphone;

    private String gender = "男";
    private Bitmap newBitmap;
    private MainApi mainApi;
    private boolean isSelectUserPhoto = false;
    private UploadManager uploadManager;

    @Override
    protected int getLayoutId() {
        return R.layout.act_register;
    }

    @Override
    protected void afterViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }

        mSwipeRefresh.setColorSchemeResources(R.color.yellow_dark);
        mSwipeRefresh.setEnabled(false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int radioId) {
                if (radioId == R.id.radio_male) {
                    radioMale.setChecked(true);
                    tvMale.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.yellow_dark));
                    tvFemale.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                    gender = "男";
                } else if (radioId == R.id.radio_female) {
                    radioFemale.setChecked(true);
                    tvFemale.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.yellow_dark));
                    tvMale.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.white));
                    gender = "女";
                }
            }
        });

        uploadManager = new UploadManager();
    }

    @OnClick({R.id.fab, R.id.tv_male, R.id.tv_female, R.id.user_photo, R.id.btn_register})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                animateRevealClose();
                break;
            case R.id.tv_male:
                radioMale.setChecked(true);
                break;
            case R.id.tv_female:
                radioFemale.setChecked(true);
                break;
            case R.id.user_photo:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_register:
                registerUser();
                break;
            default:
                break;
        }
    }

    private void registerUser() {
        mainApi = RetrofitUtils.get().create(MainApi.class);

        if (TextUtils.isEmpty(etUserphone.getText().toString())) {
            UiHelper.showToast(this, "请填写手机号");
            return;
        }

        if (!UiHelper.checkPhone(etUserphone.getText().toString())) {
            UiHelper.showToast(this, "请正确填写手机号");
            return;
        }

        if (TextUtils.isEmpty(etUsername.getText().toString())) {
            UiHelper.showToast(this, "请填写昵称");
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            UiHelper.showToast(this, "请填写密码");
            return;
        }

        if (TextUtils.isEmpty(etRepeat.getText().toString())) {
            UiHelper.showToast(this, "请重复填写密码");
            return;
        }

        if (!TextUtils.equals(etPassword.getText().toString(), etRepeat.getText().toString())) {
            UiHelper.showToast(this, "两次填写密码不正确");
            return;
        }

        if (!isSelectUserPhoto) {
            UiHelper.showToast(this, "请选择头像");
            return;
        }

        toRegister();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //进入系统图库
        switch (resultCode) {
            case Activity.RESULT_OK:
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
                cursor.moveToFirst();
                String imgPath = cursor.getString(1); // 图片文件路径
                cursor.close();

                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath, opt);
                newBitmap = UiHelper.zoomBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3);
                bitmap.recycle();
                userPhoto.setImageBitmap(newBitmap);
                isSelectUserPhoto = true;
                break;
            case Activity.RESULT_CANCELED:
                isSelectUserPhoto = false;
                break;
        }
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.mipmap.login_plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Subscribe
    private void toRegister() {
        showProgressDialog("正在注册 请稍后");
        addSubscriptions(Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(final Object obj) {
                uploadManager.put(UiHelper.bitmapTobyte(newBitmap), etUserphone.getText().toString() + ".png", MyApplication.token,
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.isOK()) {
                                    ((Subscriber) obj).onNext(key);
                                    ((Subscriber) obj).onCompleted();
                                    Log.e("bokey", "上传成功");
                                } else {
                                    QiniuErro qiniuerro = new QiniuErro(info.statusCode, info.error);
                                    ((Subscriber) obj).onError(qiniuerro);
                                    Log.e("bokey", "上传失败");
                                }
                            }
                        }, null);
            }
        }).retryWhen(new HttpTokenExpireFunc(1,1000))
                .flatMap(new Func1() {
                    @Override
                    public Object call(Object o) {
                        ArrayMap<String, String> registerInfo = new ArrayMap<>(7);
                        registerInfo.put("userPhone", etUserphone.getText().toString());
                        registerInfo.put("name", etUsername.getText().toString());
                        registerInfo.put("password", etPassword.getText().toString());
                        registerInfo.put("iconurl", etUserphone.getText().toString() + ".png");
                        registerInfo.put("gender",gender);
                        return mainApi.userRegister(registerInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        UiHelper.showToast(RegisterActivity.this, "注册失败，请重试");
                        Log.e("bokey",e.getMessage());
                    }

                    @Override
                    public void onNext(Object obj) {
                        if(((Result<UserInfo>)(obj)).code == 0){
                            UserInfo userInfo = ((Result<UserInfo>)(obj)).data;
                            UiHelper.saveUserInfo(userInfo);
                            EventBus.getDefault().postSticky(userInfo);
                            LoginActivity.instance.finish();
                            finish();
                        }else {
                            UiHelper.showToast(RegisterActivity.this,((Result<UserInfo>)(obj)).message);
                        }
                    }
                }));
    }
}
