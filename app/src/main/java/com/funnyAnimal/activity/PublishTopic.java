package com.funnyAnimal.activity;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.ImagePickerAdapter;
import com.funnyAnimal.api.HttpTokenExpireFunc;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.QiniuErro;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.PicassoImageLoader;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.LProgressDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 青峰 on 2017/7/30.
 */

public class PublishTopic extends SwipeBackActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.tv_title)
    EditText tvTitle;

    @BindView(R.id.tv_content)
    EditText tvContent;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private UploadManager uploadManager;
    private MainApi mainApi;

    private String[] upImages;
    private ImagePickerAdapter adapter;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;               //允许选择图片最大数

    @Override
    protected int getLayoutId() {
        return R.layout.act_publish_topic;
    }

    @Override
    protected void afterViews() {
        uploadManager = new UploadManager();
        mainApi = RetrofitUtils.get().create(MainApi.class);

        initWidget();
        initImagePicker();
    }

    private void initWidget() {
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    @OnClick({R.id.back_btn,R.id.btn_commit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(tvTitle.getText())) {
                    UiHelper.showToast(this, "请填写标题");
                    return;
                }

                if (TextUtils.isEmpty(tvContent.getText())) {
                    UiHelper.showToast(this, "请填写内容");
                    return;
                }
                showProgressDialog("正在发帖 请稍后");
                if (selImageList.size() > 0) {
                    upImages = new String[selImageList.size()];
                    for (int i = 0; i < selImageList.size(); i++) {
                        postImageAndContent(i);
                    }
                } else {
                    postContent();
                }
                break;
            default:
                break;
        }
    }

    ArrayList<ImageItem> images2 = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images2 = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images2 != null) {
                    selImageList.addAll(images2);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images2 = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images2 != null) {
                    selImageList.clear();
                    selImageList.addAll(images2);
                    adapter.setImages(selImageList);
                }
            }
        }
    }

    private void postImageAndContent(final int i) {
        final String fileName = UiHelper.md5(PreferenceUtil.getUserUuid()) + System.currentTimeMillis() + ".png";
        addSubscriptions(Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(final Object obj) {
                ImageItem item = selImageList.get(i);
                String itemPath = item.path;
                uploadManager.put(UiHelper.bitmapTobyte(UiHelper.Bytes2Bimap(UiHelper.getBytes(itemPath))), fileName, MyApplication.token,
                        new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.isOK()) {
                                    ((Subscriber) obj).onNext(key);
                                    ((Subscriber) obj).onCompleted();
                                    Log.e("bokey", "上传成功" + i);
                                } else {
                                    QiniuErro qiniuerro = new QiniuErro(info.statusCode, info.error);
                                    ((Subscriber) obj).onError(qiniuerro);
                                    Log.e("bokey", "上传失败" + i);
                                }
                            }
                        }, null);
            }
        }).retryWhen(new HttpTokenExpireFunc(1, 1000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer() {
                    @Override
                    public void onCompleted() {
                        int count = 0;
                        for (int i = 0; i < selImageList.size(); i++) {
                            if (null != upImages[i]) {
                                count++;
                            }
                        }

                        if (count == selImageList.size()) {
                            postContent();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        UiHelper.showToast(PublishTopic.this, "发帖失败，请重试");
                        Log.e("bokey", "发帖失败原因" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object obj) {
                        upImages[i] = fileName;
                    }
                }));
    }

    private void postContent() {
        ArrayMap<String, Object> postInfo = new ArrayMap<>(7);
        postInfo.put("uid", PreferenceUtil.getUserUid());
        postInfo.put("title", tvTitle.getText().toString());
        postInfo.put("text", tvContent.getText().toString());
        postInfo.put("imgs", upImages);
        postInfo.put("location", PreferenceUtil.getUserProvince());
        postInfo.put("cityName", PreferenceUtil.getUserCity());
        addSubscriptions(mainApi.postContent(postInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        UiHelper.showToast(PublishTopic.this, "发帖失败,请重试");
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.code == 0) {
                            UiHelper.showToast(PublishTopic.this, "发帖完成");
                            finish();
                        } else {
                            UiHelper.showToast(PublishTopic.this, result.message);
                        }
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, selImageList);///如果需要进入选择的时候显示已经选中的图片，详情请查看ImagePickerActivity
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
}
