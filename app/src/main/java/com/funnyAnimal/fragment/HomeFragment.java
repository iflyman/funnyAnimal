package com.funnyAnimal.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.TabsAdapter;
import com.funnyAnimal.api.DialyShow;
import com.funnyAnimal.api.MainApi;
import com.funnyAnimal.api.Result;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.utils.ListUtils;
import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.view.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by bhyan on 2017/10/24.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.dot_group)
    LinearLayout dotGroup;

    @BindView(R.id.mSwipeRefresh)
    MultiSwipeRefreshLayout mSwipeRefresh;

    private MainApi mainApi;
    private TabsAdapter tabsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void afterViews() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setEnabled(false);
        mainApi = RetrofitUtils.get().create(MainApi.class);
        toGetDialyShows();
        List data = new ArrayList<DialyShow>();
        DialyShow dialyShow = new DialyShow();
        dialyShow.type = Constant.TYPEPIC;
        dialyShow.text = "春江潮水连海平 海上明月共潮生";
        dialyShow.metaUrl ="";
        dialyShow.imgUrl = "http://os76ha42j.bkt.clouddn.com/05.jpg";
        dialyShow.date = "20181022";
        dialyShow.author = "波波";
        data.add(dialyShow);
        data.add(dialyShow);
        data.add(dialyShow);
        data.add(dialyShow);
        setFragment(data);
    }


    private void toGetDialyShows() {
        UiHelper.setRefreshing(mSwipeRefresh, true);
        addSubscriptions(mainApi.queryDialyShows("funny")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<DialyShow>>>() {
                    @Override
                    public void onCompleted() {
                        UiHelper.setRefreshing(mSwipeRefresh, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        UiHelper.setRefreshing(mSwipeRefresh, false);
                        UiHelper.showToast(getContext(), "您的网络有问题，请稍后重试");
                    }

                    @Override
                    public void onNext(Result<List<DialyShow>> listResult) {
                        if (listResult.code == 0 && !ListUtils.isEmpty(listResult.data)) {
                            setFragment(listResult.data);
                        } else {
                            UiHelper.showToast(getContext(), listResult.message);
                        }
                    }
                }));
    }

    private void setFragment(final List<DialyShow> data) {
        if (null == tabsAdapter) {
            tabsAdapter = new TabsAdapter(getActivity().getSupportFragmentManager());
        }

        for (int i = 0; i < data.size(); i++) {
            DialyShow dialyShow = data.get(i);
            Bundle bundle = new Bundle(6);
            bundle.putString("imgUrl", dialyShow.imgUrl);
            bundle.putString("author", dialyShow.author);
            bundle.putString("date", dialyShow.date);
            bundle.putString("text", dialyShow.text);
            bundle.putString("metaUrl", dialyShow.metaUrl);
            bundle.putInt("type", dialyShow.type);
            tabsAdapter.addFragment(ContentFragment.newInstance(bundle));
        }

        for (int i = 0; i < data.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 0, 0);
            imageView.setLayoutParams(params);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.circle_dot_choose);
            } else imageView.setBackgroundResource(R.drawable.circle_dot);
            dotGroup.addView(imageView);
        }
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < data.size(); i++) {
                    if (i == position)
                        dotGroup.getChildAt(i).setBackgroundResource(R.drawable.circle_dot_choose);
                    else
                        dotGroup.getChildAt(i).setBackgroundResource(R.drawable.circle_dot);
                }
            }
        });
    }
}
