package com.funnyAnimal.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.funnyAnimal.R;
import com.funnyAnimal.adapter.RecyclingPagerAdapter;
import com.funnyAnimal.utils.ImageLoadFactory;
import com.funnyAnimal.utils.ViewUtils;
import com.funnyAnimal.view.TouchImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by 青峰 on 2017/9/24.
 */

public class ImageGalleryActivity extends SwipeBackActivity implements ViewPager.OnPageChangeListener{
    public static final String IMAGES = "_images";
    public static final String POSITION = "_position";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image_gallery)
    ViewPager mViewPager;

    private String tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_image_gallery;
    }

    @Override
    protected void afterViews() {
        String[] images = getIntent().getStringArrayExtra(IMAGES);
        tips = "%1$d/" + images.length;
        final int position = getIntent().getIntExtra(POSITION, 0);
        toolbar.setTitle(String.format(tips, position + 1));
        setSupportToolbar(toolbar);
        mViewPager.setAdapter(new ImagePaperAdapter(images));
        mViewPager.setCurrentItem(position, false);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == View.SYSTEM_UI_FLAG_VISIBLE) {
                    toolbar.setPadding(0, ViewUtils.getStatusBarHeight(ImageGalleryActivity.this), 0, 0);
                } else {
                    toolbar.setPadding(0, 0, 0, 0);
                }
            }
        });
        ViewUtils.hideSystemUi(mViewPager);
    }

    public void toggleToolbar() {
        if (null == toolbar)
            return;
        if (toolbar.getTranslationY() == 0) {
            ViewCompat.animate(toolbar).translationY(-toolbar.getHeight())
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setDuration(500)
                    .start();
            ViewUtils.hideSystemUi(mViewPager);
        } else {
            ViewCompat.animate(toolbar).translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setDuration(500)
                    .start();
            ViewUtils.showSystemUi(mViewPager);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        toolbar.setTitle(String.format(tips, position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            ViewUtils.hideSystemUi(mViewPager);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.clearOnPageChangeListeners();
        Picasso.with(this).cancelTag(this);
        super.onDestroy();
    }

    final class ImagePaperAdapter extends RecyclingPagerAdapter {

        private String[] items;

        ImagePaperAdapter(String[] images) {
            this.items = images;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                TouchImageView photoView = new TouchImageView(parent.getContext());
                holder = new ViewHolder(photoView);
                convertView = holder.photoView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoadFactory.display2(ImageGalleryActivity.this, items[position], holder.photoView);
            return convertView;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.length;
        }

        class ViewHolder implements View.OnClickListener {
            TouchImageView photoView;

            public ViewHolder(TouchImageView photoView) {
                this.photoView = photoView;
                this.photoView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                ImageGalleryActivity.this.toggleToolbar();
            }
        }
    }
}
