package com.funnyAnimal.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.view.View;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.funnyAnimal.R;
import com.funnyAnimal.api.DialyShow;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.forum.ForumActivity;
import com.funnyAnimal.story.StoryActivity;
import com.funnyAnimal.user.UserLoginActivity;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.view.NetworkImageHolderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2018/6/11.
 */

public class HomeActivity extends BaseActivity {

    @BindView(R.id.banner)
    ConvenientBanner banner;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.dot_group)
    LinearLayout dotGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.act_home;
    }

    @Override
    protected void afterViews() {
        toShowDaily();
    }

    private void toShowDaily() {
        List<String> ads = new ArrayList<>();
        ads.add("http://os76ha42j.bkt.clouddn.com/05.jpg");
        ads.add("http://os76ha42j.bkt.clouddn.com/12.jpg");
        ads.add("http://os76ha42j.bkt.clouddn.com/05.jpg");
        ads.add("http://os76ha42j.bkt.clouddn.com/12.jpg");

        banner.startTurning(10000).setPageIndicator(new int[]{R.drawable.banner_point, R.drawable.banner_point_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPages(new CBViewHolderCreator() {
                    @Override
                    public Holder createHolder(View itemView) {
                        return new NetworkImageHolderView(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_home_image;
                    }
                }, ads).setOnItemClickListener(null);
        DialyShow dialyShow = new DialyShow();
        dialyShow.author = "authoer";
        dialyShow.date = "1530772391";
        dialyShow.imgUrl = "https://www.pexels.com/photo/close-up-photo-of-gray-concrete-road-1197095/";
        dialyShow.metaUrl = "";
        dialyShow.text = "text";
        dialyShow.type = 1;
        List<DialyShow> dialyShows = new ArrayList<>();
        dialyShows.add(dialyShow);
        dialyShows.add(dialyShow);
    }

    @OnClick({R.id.toLogin, R.id.btnStory,R.id.btnForum})
    void toClick(View view) {
        switch (view.getId()) {
            case R.id.toLogin:
                startActivity(new Intent(this, UserLoginActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                getWindow().setEnterTransition(new Fade().setDuration(Constant.Duration));
                getWindow().setExitTransition(new Fade().setDuration(Constant.Duration));
                break;
            case R.id.btnStory:
                startActivity(new Intent(this, StoryActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                getWindow().setEnterTransition(new Fade().setDuration(Constant.Duration));
                getWindow().setExitTransition(new Fade().setDuration(Constant.Duration));
                break;
            case R.id.btnForum:
                startActivity(new Intent(this, ForumActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                getWindow().setEnterTransition(new Fade().setDuration(Constant.Duration));
                getWindow().setExitTransition(new Fade().setDuration(Constant.Duration));
                break;
            default:
                break;
        }
    }
}
