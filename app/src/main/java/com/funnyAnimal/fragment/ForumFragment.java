package com.funnyAnimal.fragment;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.dudu0118.superrefreshlib.view.SuperRefreshLayout;
import com.funnyAnimal.R;
import com.funnyAnimal.activity.LoginActivity;
import com.funnyAnimal.activity.PublishTopic;
import com.funnyAnimal.activity.TopicDetailActivity;
import com.funnyAnimal.activity.TopicTypeActivity;
import com.funnyAnimal.api.BannerEntity;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.api.TopicEntity;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.ListUtils;
import com.funnyAnimal.utils.PreferenceUtil;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.NetworkImageHolderView;
import com.funnyAnimal.view.recycle.OnItemClickListener;
import com.funnyAnimal.view.recycle.RecyclerAdapter;
import com.funnyAnimal.view.recycle.RecyclerViewHolder;
import com.funnyAnimal.view.recycle.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bhyan on 2017/10/24.
 */

public class ForumFragment extends BaseFragment implements OnItemClickListener {
    @BindView(R.id.banner)
    ConvenientBanner banner;

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
        return R.layout.fragment_forum;
    }

    @Override
    protected void afterViews() {
        mainApi = RetrofitUtils.get().create(MainApi.class);
        mSuperRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuperRecyclerView.setBackgroundResource(R.drawable.background_white);
        mSuperRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mSuperRecyclerView.enable(true);
        mSuperRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new RecyclerAdapter<TopicEntity>(getContext(), R.layout.item_my_topic) {
            @Override
            protected void bindData(RecyclerViewHolder holder, TopicEntity topicEntity, int position) {
                if(null != topicEntity.userInfoSay.userInfo){
                    holder.getText(R.id.topic_title).setText(topicEntity.userInfoSay.userInfo.name);
                    ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.userInfo.iconurl.replace("https", "http"), holder.getCircleImageView(R.id.user_photo));
                }
                holder.getText(R.id.topic_content).setText(topicEntity.userInfoSay.say.title);
                if (!ListUtils.isEmpty(topicEntity.userInfoSay.say.imgs)) {
                    int size = topicEntity.userInfoSay.say.imgs.size();
                    holder.getLinearLayout(R.id.img_rl).setVisibility(View.VISIBLE);
                    if (size == 1) {
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        holder.getImageView(R.id.topic_image2).setVisibility(View.INVISIBLE);
                        holder.getImageView(R.id.topic_image3).setVisibility(View.INVISIBLE);
                    } else if (size == 2) {
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(1).replace("https", "http"), holder.getImageView(R.id.topic_image2));
                        holder.getImageView(R.id.topic_image3).setVisibility(View.INVISIBLE);
                    } else if (size >= 3) {
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(0).replace("https", "http"), holder.getImageView(R.id.topic_image1));
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(1).replace("https", "http"), holder.getImageView(R.id.topic_image2));
                        ImageLoadFactory.display2(getContext(), topicEntity.userInfoSay.say.imgs.get(2).replace("https", "http"), holder.getImageView(R.id.topic_image3));
                    }
                } else {
                    holder.getLinearLayout(R.id.img_rl).setVisibility(View.GONE);
                }
                holder.getText(R.id.comment_count).setText(topicEntity.userInfoSay.say.count);
            }
        };
        mSuperRecyclerView.setAdapter(mAdapter);
        mSuperRecyclerView.setFocusable(false);
        mAdapter.setOnItemClickListener(this);
        getAds();
        getHotTopic();
        refresh.setRefresh(true);
        refresh.setRefreshListener(new SuperRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh(SuperRefreshLayout superRefreshLayout) {
                page= 1;
                mAdapter.clear();
                mSuperRecyclerView.showHide();
                getHotTopic();
                getAds();

            }

            @Override
            public void onRefreshLoadMore(SuperRefreshLayout superRefreshLayout) {
                getHotTopic();
            }
        });
    }

    private void getAds() {
        addSubscriptions(mainApi.getBanner()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<BannerEntity>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiHelper.showToast(getContext(),"网络暂时有点问题，请稍后重试");
                        Log.e("bokey",e.getMessage());
                    }

                    @Override
                    public void onNext(Result<List<BannerEntity>> result) {
                        if(result.code == 0 && !ListUtils.isEmpty(result.data)){
                            showAds(result.data);
                        }else {
                            UiHelper.showToast(getContext(),result.message);
                        }
                    }
                }));
    }

    private void showAds(final List<BannerEntity> entities) {
        List<String> ads = new ArrayList<>();
        for(int i = 0 ; i < entities.size() ; i++){
            ads.add(entities.get(i).bannerUrl);
        }
//        banner.startTurning(5000).setPageIndicator(new int[]{R.drawable.banner_point, R.drawable.banner_point_selected})
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setPages(new CBViewHolderCreator() {
//                    @Override
//                    public Object createHolder() {
//                        return new NetworkImageHolderView();
//                    }
//                }, ads).setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                if(entities.get(position).type == Constant.TYPETOPIC){
//                    startActivity(new Intent(getContext(),TopicDetailActivity.class).putExtra("tid",entities.get(position).bannerText));
//                }
//            }
//        });
    }

    private void getHotTopic() {
        showProgressDialog("正在加载数据...");
        addSubscriptions(mainApi.queryHotTopic(page, maxCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<TopicEntity>>>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        UiHelper.showToast(getContext(), "网络超时，请重试");
                    }

                    @Override
                    public void onNext(Result<List<TopicEntity>> result) {
                        if (result.code == 0 && !ListUtils.isEmpty(result.data)) {
                            showTopic(result.data);
                            page++;
                            if (result.data.size() < maxCount) {
                                mSuperRecyclerView.showLoadEnd();
                            } else {
                                mSuperRecyclerView.showHide();
                            }
                        } else {
                            mSuperRecyclerView.showLoadEnd();
                            UiHelper.showToast(getContext(), result.message);
                        }
                    }
                }));
    }

    private void showTopic(List<TopicEntity> topicEntityList) {
        mAdapter.addAll(topicEntityList);
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getContext(), TopicDetailActivity.class).putExtra("tid", mAdapter.getItem(position).userInfoSay.say.tid));
    }

    @OnClick({R.id.add_topic, R.id.newest_topic, R.id.offical_topic, R.id.my_topic})
    void Onclick(View view) {
        switch (view.getId()) {
            case R.id.add_topic:
                if(TextUtils.isEmpty(PreferenceUtil.getUserUid())){
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getContext(), PublishTopic.class));
                break;
            case R.id.my_topic:
                if(TextUtils.isEmpty(PreferenceUtil.getUserUid())){
                    UiHelper.showToast(getContext(),"请登录您的账号");
                    startActivity(new Intent(getContext(),LoginActivity.class));
                   return;
                }
                startActivity(new Intent(getContext(), TopicTypeActivity.class).putExtra("type", "my"));
                break;
            case R.id.newest_topic:
                startActivity(new Intent(getContext(), TopicTypeActivity.class).putExtra("type", "newest"));
                break;
            case R.id.offical_topic:
                startActivity(new Intent(getContext(), TopicTypeActivity.class).putExtra("type", "offical"));
                break;
            default:
                break;
        }
    }
}
