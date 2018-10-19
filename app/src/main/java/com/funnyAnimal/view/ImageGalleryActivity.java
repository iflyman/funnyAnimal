package com.funnyAnimal.view;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.base.BaseActivity;
import com.funnyAnimal.utils.ImageLoadFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhyan on 2017/5/4.
 */

public class ImageGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    public static final String IMAGES = "_images";
    public static final String POSITION = "_position";

     private TextView title;
     private ViewPager mViewPager;

    private String tips;

    @Override
    protected int getLayoutId() {
        return R.layout.act_image_gallery;
    }

    @Override
    protected void afterViews() {
        title = (TextView) findViewById(R.id.title);
        mViewPager = (ViewPager) findViewById(R.id.image_gallery);
        String[] images = getIntent().getStringArrayExtra(IMAGES);
        tips = "%1$d/" + images.length;
        final int position = getIntent().getIntExtra(POSITION, 0);
        title.setText(String.format(tips, position + 1));
        mViewPager.setAdapter(new ImagePagerAdapter(images));
        mViewPager.setCurrentItem(position, false);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        title.setText(String.format(tips, position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDestroy() {
        mViewPager.clearOnPageChangeListeners();
        super.onDestroy();
    }

    final class ImagePagerAdapter extends PagerAdapter{

        private List<View> items = new ArrayList<>();

        ImagePagerAdapter(String[] images) {
            for(int i =0;i<images.length;i++){
                ImageView photoView = new ImageView(ImageGalleryActivity.this);
                ImageLoadFactory.display2(ImageGalleryActivity.this,images[i],photoView);
                items.add(photoView);
            }
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(items.get(position));
            return items.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(items.get(position));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
