package com.funnyAnimal.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.dudu0118.superrefreshlib.view.SuperRefreshLayout;
import com.funnyAnimal.R;
import com.funnyAnimal.activity.TopicDetailActivity;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.TopicEntity;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.recycle.OnItemClickListener;
import com.funnyAnimal.view.recycle.RecyclerAdapter;
import com.funnyAnimal.view.recycle.RecyclerViewHolder;
import com.funnyAnimal.view.recycle.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by bhyan on 2018/10/22.
 */

public class ForumInfoFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R.id.mSuperRecyclerView)
    SuperRecyclerView mSuperRecyclerView;

    @BindView(R.id.refresh)
    SuperRefreshLayout refresh;

    private RecyclerAdapter<TopicEntity> mAdapter;
    private MainApi mainApi;
    private int page = 1;
    private int maxCount = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_forum_info;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
        mSuperRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuperRecyclerView.setBackgroundResource(R.drawable.background_white);
        mSuperRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mSuperRecyclerView.enable(true);
        mSuperRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new RecyclerAdapter<TopicEntity>(getContext(), R.layout.item_topic_card) {
            @Override
            protected void bindData(RecyclerViewHolder holder, TopicEntity topicEntity, int position) {
                holder.getImageView(R.id.iv_back).setBackgroundResource(R.mipmap.img_card_back1);
            }
        };
        mSuperRecyclerView.setAdapter(mAdapter);
        mSuperRecyclerView.setFocusable(false);
        mAdapter.setOnItemClickListener(this);
        getHotTopic();
        refresh.setRefresh(true);
        refresh.setRefreshListener(new SuperRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(SuperRefreshLayout superRefreshLayout) {
                page = 1;
//                mAdapter.clear();
                mSuperRecyclerView.showHide();
                getHotTopic();

            }

            @Override
            public void onRefreshLoadMore(SuperRefreshLayout superRefreshLayout) {
                getHotTopic();
            }
        });

        List<TopicEntity> topicEntityList = new ArrayList<>();
        TopicEntity topicEntity = new TopicEntity();
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        topicEntityList.add(topicEntity);
        mAdapter.addAll(topicEntityList);
    }

    private void getHotTopic() {
        UiHelper.showToast(getContext(), "暂时没有数据库支持");
//        showProgressDialog("正在加载数据...");
//        addSubscriptions(mainApi.queryHotTopic(page, maxCount)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Result<List<TopicEntity>>>() {
//                    @Override
//                    public void onCompleted() {
//                        dismissProgressDialog();
//                        refresh.finishLoadMore();
//                        refresh.finishRefresh();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dismissProgressDialog();
//                        refresh.finishLoadMore();
//                        refresh.finishRefresh();
//                        UiHelper.showToast(getContext(), "网络超时，请重试");
//                    }
//
//                    @Override
//                    public void onNext(Result<List<TopicEntity>> result) {
//                        if (result.code == 0 && !ListUtils.isEmpty(result.data)) {
//                            showTopic(result.data);
//                            page++;
//                            if (result.data.size() < maxCount) {
//                                mSuperRecyclerView.showLoadEnd();
//                            } else {
//                                mSuperRecyclerView.showHide();
//                            }
//                        } else {
//                            mSuperRecyclerView.showLoadEnd();
//                            UiHelper.showToast(getContext(), result.message);
//                        }
//                    }
//                }));
    }

    private void showTopic(List<TopicEntity> topicEntityList) {
        mAdapter.addAll(topicEntityList);
    }

    @Override
    public void onItemClick(View view, int position) {
        UiHelper.showToast(getContext(),"点击还没做好呢");
//        startActivity(new Intent(getContext(), TopicDetailActivity.class).putExtra("tid", mAdapter.getItem(position).userInfoSay.say.tid));
    }
}
