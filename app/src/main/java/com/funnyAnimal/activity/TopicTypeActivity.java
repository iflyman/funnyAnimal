package com.funnyAnimal.activity;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.TopicEntity;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.ListUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
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

public class TopicTypeActivity extends SwipeBackActivity implements OnItemClickListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.mSuperRecyclerView)
    SuperRecyclerView mSuperRecyclerView;

    private String type;
    private RecyclerAdapter<TopicEntity> mAdapter;
    private MainApi mainApi;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.act_topic_type;
    }

    @Override
    protected void afterViews() {
        type = getIntent().getStringExtra("type");
        mainApi = RetrofitUtils.get().create(MainApi.class);
        mSuperRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerView.setBackgroundResource(R.drawable.background_white);
        mSuperRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSuperRecyclerView.enable(true);
        mAdapter = new RecyclerAdapter<TopicEntity>(this, R.layout.item_my_topic) {
            @Override
            protected void bindData(RecyclerViewHolder holder, TopicEntity topicEntity, int position) {
                if(null != topicEntity.userInfoSay.userInfo){
                    holder.getText(R.id.topic_title).setText(topicEntity.userInfoSay.userInfo.name);
                    ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.userInfo.iconurl.replace("https", "http"), holder.getCircleImageView(R.id.user_photo));
                }
                holder.getText(R.id.topic_content).setText(topicEntity.userInfoSay.say.title);
                if (!ListUtils.isEmpty(topicEntity.userInfoSay.say.imgs)) {
                    int size = topicEntity.userInfoSay.say.imgs.size();
                    holder.getLinearLayout(R.id.img_rl).setVisibility(View.VISIBLE);
                    if (size == 1) {
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        holder.getImageView(R.id.topic_image2).setVisibility(View.INVISIBLE);
                        holder.getImageView(R.id.topic_image3).setVisibility(View.INVISIBLE);
                    } else if (size == 2) {
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(1).replace("https", "http"), holder.getImageView(R.id.topic_image2));
                        holder.getImageView(R.id.topic_image3).setVisibility(View.INVISIBLE);
                    } else if (size >= 3) {
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(1).replace("https", "http"), holder.getImageView(R.id.topic_image2));
                        ImageLoadFactory.display2(TopicTypeActivity.this, topicEntity.userInfoSay.say.imgs.get(2).replace("https", "http"), holder.getImageView(R.id.topic_image3));
                    }
                } else {
                    holder.getLinearLayout(R.id.img_rl).setVisibility(View.GONE);
                }
                holder.getText(R.id.comment_count).setText(topicEntity.userInfoSay.say.count);
            }
        };
        mSuperRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mSuperRecyclerView.setOnLoadMoreListener(new SuperRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (TextUtils.equals(type, "newest")) {
                    title.setText("最新发布");
                    getTopic(null);
                } else if (TextUtils.equals(type, "my")) {
                    title.setText("我的主题");
                    getTopic(PreferenceUtil.getUserUid());
                } else {
                    title.setText("官方发布");
                    getTopic("3D8E8DC523A7A93519F87A13956C86AF");
                }
            }
        });
        mSuperRecyclerView.addOnScrollListener(new SampleScrollListener(this));
        if (TextUtils.equals(type, "newest")) {
            title.setText("最新发布");
            getTopic(null);
        } else if (TextUtils.equals(type, "my")) {
            title.setText("我的主题");
            getTopic(PreferenceUtil.getUserUid());
        } else {
            title.setText("官方发布");
            getTopic("3D8E8DC523A7A93519F87A13956C86AF");
        }
    }

    private void getTopic(String uid) {
        showProgressDialog("正在加载数据...");
        addSubscriptions(mainApi.queryTopic(page, 4,uid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<TopicEntity>>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        UiHelper.showToast(TopicTypeActivity.this, "网络超时，请重试");
                    }

                    @Override
                    public void onNext(Result<List<TopicEntity>> result) {
                        if (result.code == 0 && !ListUtils.isEmpty(result.data)) {
                            showTopic(result.data);
                            page++;
                            if (result.data.size() < 4) {
                                mSuperRecyclerView.showLoadEnd();
                            } else {
                                mSuperRecyclerView.showHide();
                            }
                        } else {
                            mSuperRecyclerView.showLoadEnd();
                            UiHelper.showToast(TopicTypeActivity.this, result.message);
                        }
                    }
                }));
    }

    private void showTopic(List<TopicEntity> topicEntityList) {
        mAdapter.addAll(topicEntityList);
    }

    @OnClick(R.id.back_btn)
    void OnClick() {
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(this, TopicDetailActivity.class).putExtra("tid", mAdapter.getItem(position).userInfoSay.say.tid));
    }
}
