package com.funnyAnimal.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.api.HttpTokenExpireFuncModifyPhoto;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.QiniuErro;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.UserInfo;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by bhyan on 2017/8/15.
 */

public class ModifyUserInfoActivity extends SwipeBackActivity {

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

    private static final int AREA_CODE = 0;
    private static final int NAME_MODIFY = 1;
    private static final int GENDER_MODIFY = 2;
    private static final int CHOOSE_PICTURE = 3;
    private UploadManager uploadManager;
    private MainApi mainApi;

    @Override
    protected int getLayoutId() {
        return R.layout.act_modify_user;
    }

    @Override
    protected void afterViews() {
        uploadManager = new UploadManager();
        mainApi = RetrofitUtils.get().create(MainApi.class);
        if (!TextUtils.isEmpty(PreferenceUtil.getUserBitmapUrl())) {
            ImageLoadFactory.display2(this, PreferenceUtil.getUserBitmapUrl().replace("https", "http"), userPhoto);
        }
        tvName.setText(PreferenceUtil.getUserName());
        tvGender.setText(PreferenceUtil.getUserGender());
        tvArea.setText(PreferenceUtil.getUserProvince() + "-" + PreferenceUtil.getUserCity());
        tvMoto.setText(PreferenceUtil.getUserMoto());
    }

    @OnClick({R.id.back_btn, R.id.choose_area, R.id.modify_name, R.id.modify_gender, R.id.modify_moto, R.id.photo_rl})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.choose_area:
                Intent intent = new Intent(this, GetProviceActivity.class);
                startActivityForResult(intent, AREA_CODE);
                break;
            case R.id.modify_name:
                Intent intentName = new Intent(this, ModifyNameActivity.class).putExtra("type", "name");
                startActivityForResult(intentName, NAME_MODIFY);
                break;
            case R.id.modify_moto:
                Intent intentMoto = new Intent(this, ModifyNameActivity.class).putExtra("type", "moto");
                startActivityForResult(intentMoto, NAME_MODIFY);
                break;
            case R.id.modify_gender:
                Intent intentSex = new Intent(this, ModifyGenderActivity.class);
                startActivityForResult(intentSex, GENDER_MODIFY);
                break;
            case R.id.photo_rl:
                Intent intentPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentPhoto, CHOOSE_PICTURE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AREA_CODE:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    String provice = bundle.getString("provice");
                    String cityName = bundle.getString("cityName");
                    tvArea.setText(provice + " " + cityName);
                }
                break;
            case NAME_MODIFY:
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    String type = bundle.getString("type");
                    if (TextUtils.equals(type, "name")) {
                        tvName.setText(bundle.getString("data"));
                    } else if (TextUtils.equals(type, "moto")) {
                        tvMoto.setText(bundle.getString("data"));
                    }
                }
                break;
            case GENDER_MODIFY:
                if (null != data) {
                    String gender = data.getStringExtra("gender");
                    tvGender.setText(gender);
                }
                break;
            case CHOOSE_PICTURE:
                if(null != data) {
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
                    cursor.moveToFirst();
                    String imgPath = cursor.getString(1); // 图片文件路径
                    cursor.close();

                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inSampleSize = 3;
                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath, opt);
                    toCommitUserPhoto(UiHelper.zoomBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3));
                    bitmap.recycle();
                }
                break;
            default:
                break;
        }
    }

    private void toCommitUserPhoto(final Bitmap bitmap) {
        showProgressDialog("请稍等...");
        addSubscriptions(Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(final Object obj) {
                uploadManager.put(UiHelper.bitmapTobyte(bitmap), PreferenceUtil.getUserUuid() + ".png", MyApplication.modifyToken,
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
        }).retryWhen(new HttpTokenExpireFuncModifyPhoto(1, 1000))
                .flatMap(new Func1() {
                    @Override
                    public Object call(Object o) {
                        ArrayMap<String, String> params = new ArrayMap<>(7);
                        params.put("iconurl", PreferenceUtil.getUserUuid() + ".png");
                        params.put("accessToken",PreferenceUtil.getUserUuid());
                        return mainApi.userModify(params);
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
                        UiHelper.showToast(ModifyUserInfoActivity.this, "网络暂时无法访问，请稍后重试");
                    }

                    @Override
                    public void onNext(Object obj) {
                        if (((Result<UserInfo>) (obj)).code == 0) {
                            UserInfo userInfo = ((Result<UserInfo>) (obj)).data;
                            PreferenceUtil.setUserBimapUrl(userInfo.iconurl);
                            ImageLoadFactory.display2(ModifyUserInfoActivity.this, userInfo.iconurl.replace("https", "http"), userPhoto);
                            bitmap.recycle();
                        } else {
                            UiHelper.showToast(ModifyUserInfoActivity.this, ((Result<UserInfo>) (obj)).message);
                        }
                    }
                }));
    }
}
