package com.funnyAnimal.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu0118.superrefreshlib.view.SuperRefreshLayout;
import com.funnyAnimal.R;
import com.funnyAnimal.adapter.TopicGridAdapter;
import com.funnyAnimal.api.CommentEntity;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.TopicDetailEntity;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.ListUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.CircleImageView;
import com.funnyAnimal.view.recycle.OnItemClickListener;
import com.funnyAnimal.view.recycle.RecyclerAdapter;
import com.funnyAnimal.view.recycle.RecyclerViewHolder;
import com.funnyAnimal.view.recycle.SampleScrollListener;
import com.funnyAnimal.view.recycle.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by 青峰 on 2017/8/6.
 */

public class TopicDetailActivity extends SwipeBackActivity implements AdapterView.OnItemClickListener, OnItemClickListener {

    @BindView(R.id.user_photo)
    CircleImageView userPhoto;

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.mGridView)
    GridView mGridView;

    @BindView(R.id.tv_reback)
    TextView tvReback;

    @BindView(R.id.edit_comment)
    EditText editComment;

    @BindView(R.id.mSuperRecyclerView)
    SuperRecyclerView mSuperRecyclerView;

    @BindView(R.id.user_gender)
    ImageView userGender;

    @BindView(R.id.refresh)
    SuperRefreshLayout refresh;

    private String tid;
    private MainApi mainApi;
    private TopicGridAdapter topicGridAdapter;
    private int page = 0;
    private RecyclerAdapter<CommentEntity> mAdapter;
    private String lid;

    @Override
    protected int getLayoutId() {
        return R.layout.act_topic_detail;
    }

    @Override
    protected void afterViews() {
        tid = getIntent().getStringExtra("tid");
        mainApi = RetrofitUtils.get().create(MainApi.class);
        getTopicDetail();
        getComment();
        mSuperRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerView.setBackgroundResource(R.drawable.background_white);
        mSuperRecyclerView.enable(true);
        mSuperRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new RecyclerAdapter<CommentEntity>(this, R.layout.item_comment) {
            @Override
            protected void bindData(RecyclerViewHolder holder, CommentEntity commentEntity, int position) {
                ImageLoadFactory.display2(TopicDetailActivity.this, commentEntity.publishLogo.replace("https", "http"), holder.getCircleImageView(R.id.user_photo));
                holder.getText(R.id.tv_name).setText(commentEntity.publishName);
                if (commentEntity.publishGender.equals("男")) {
                    holder.getImageView(R.id.icon_gender).setBackgroundResource(R.mipmap.icon_male);
                } else {
                    holder.getImageView(R.id.icon_gender).setBackgroundResource(R.mipmap.icon_female);
                }
                holder.getText(R.id.tv_date).setText(UiHelper.time2Date(commentEntity.timestamp));
                holder.getText(R.id.tv_content).setText(commentEntity.description);
                if (null == commentEntity.parentPublish) {
                    holder.getLinearLayout(R.id.parent_li).setVisibility(View.GONE);
                } else {
                    holder.getLinearLayout(R.id.parent_li).setVisibility(View.VISIBLE);
                    holder.getText(R.id.tv_parent_name).setText(commentEntity.parentPublish.publishName);
                    holder.getText(R.id.tv_parent_content).setText(commentEntity.parentPublish.description);
                }
            }
        };
        mSuperRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mSuperRecyclerView.addOnScrollListener(new SampleScrollListener(this));
        refresh.setRefresh(false);
        refresh.setRefreshListener(new SuperRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(SuperRefreshLayout superRefreshLayout) {
                page = 0;
                getComment();
            }

            @Override
            public void onRefreshLoadMore(SuperRefreshLayout superRefreshLayout) {
                getComment();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        lid = mAdapter.getItem(position).publishId;
        editComment.setHint("回复" + mAdapter.getItem(position).publishName + ":");
    }

    private void getTopicDetail() {
        showProgressDialog("正在加载数据...");
        addSubscriptions(mainApi.getTopicDetail(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<TopicDetailEntity>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        UiHelper.showToast(TopicDetailActivity.this, "网络超时，请重试");
                        Log.e("bokey", "发生错误" + e.getMessage());
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(Result<TopicDetailEntity> result) {
                        if (result.code == 0 && null != result.data) {
                            toShowTopicDetail(result.data);
                        } else {
                            UiHelper.showToast(TopicDetailActivity.this, result.message);
                        }
                    }
                }));
    }

    private void toShowTopicDetail(final TopicDetailEntity topicDetailEntity) {
        ImageLoadFactory.display2(this, topicDetailEntity.userInfo.iconurl.replace("https", "http"), userPhoto);
        userName.setText(topicDetailEntity.userInfo.name);
        time.setText(UiHelper.time2Date(topicDetailEntity.say.timestamp));
        location.setText(topicDetailEntity.say.location + " - " + topicDetailEntity.say.cityName);
        tvTitle.setText(topicDetailEntity.say.title);
        tvContent.setText(topicDetailEntity.say.text);
        if (TextUtils.equals(topicDetailEntity.userInfo.gender, "男")) {
            userGender.setBackgroundResource(R.mipmap.icon_male);
        } else {
            userGender.setBackgroundResource(R.mipmap.icon_female);
        }
        if (null != topicDetailEntity.say.imgs) {
            topicGridAdapter = new TopicGridAdapter(this, topicDetailEntity.say.imgs);
            mGridView.setAdapter(topicGridAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] urls = new String[topicDetailEntity.say.imgs.size()];
                    Bundle extras = new Bundle(2);
                    for (int i = 0; i < topicDetailEntity.say.imgs.size(); i++) {
                        urls[i] = topicDetailEntity.say.imgs.get(i).replace("https", "http");
                    }
                    extras.putStringArray(ImageGalleryActivity.IMAGES, urls);
                    extras.putInt(ImageGalleryActivity.POSITION, position);
                    startActivity(new Intent(TopicDetailActivity.this, ImageGalleryActivity.class).putExtras(extras));
                }
            });
        }
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_reback);
        drawable.setBounds(0, 0, UiHelper.dp2px(this, 15), UiHelper.dp2px(this, 15));
        tvReback.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void getComment() {
        addSubscriptions(mainApi.getTopicComment(tid, page, 5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<CommentEntity>>>() {
                    @Override
                    public void onCompleted() {
                        refresh.finishLoadMore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        refresh.finishLoadMore();
                        UiHelper.showToast(TopicDetailActivity.this, "网络超时，请重试");
                        Log.e("bokey", "发生错误" + e.getMessage());
                    }

                    @Override
                    public void onNext(Result<List<CommentEntity>> result) {
                        if (result.code == 0 && !ListUtils.isEmpty(result.data)) {
                            showComment(result.data);
                            page++;
                            if (result.data.size() < 5) {
                                mSuperRecyclerView.showLoadEnd();
                            } else {
                                mSuperRecyclerView.showHide();
                            }
                        } else {
                            mSuperRecyclerView.showLoadEnd();
                            UiHelper.showToast(TopicDetailActivity.this, result.message);
                        }
                    }
                }));
    }

    private void showComment(List<CommentEntity> commentEntities) {
        mAdapter.addAll(commentEntities);
    }

    @OnClick({R.id.back_btn, R.id.btn_send})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.btn_send:
                if (TextUtils.isEmpty(editComment.getText().toString())) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    if(TextUtils.isEmpty(PreferenceUtil.getUserUid())){
                        startActivity(new Intent(this,LoginActivity.class));
                        return;
                    }
                    toComment();
                }
                break;
            default:
                break;
        }
    }

    private void toComment() {
        showProgressDialog("请稍等...");
        ArrayMap<String, String> params = new ArrayMap<>(4);
        params.put("tid", tid);
        params.put("leaveId", PreferenceUtil.getUserUid());
        params.put("text", editComment.getText().toString());
        params.put("lid", lid);
        addSubscriptions(mainApi.toComment(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        UiHelper.showToast(TopicDetailActivity.this, "发帖失败，请重试");
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.code == 0) {
                            UiHelper.showToast(TopicDetailActivity.this, "发帖成功");
                            page = 0;
                            editComment.setHint("");
                            editComment.setText("");
                            mAdapter.clear();
                            lid = "";
                            getComment();
                        } else {
                            UiHelper.showToast(TopicDetailActivity.this, result.message);
                        }
                    }
                }));
    }
}
