package com.funnyAnimal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyAnimal.R;
import com.funnyAnimal.activity.ImageGalleryActivity;
import com.funnyAnimal.activity.WebviewActivity;
import com.funnyAnimal.base.BaseFragment;
import com.funnyAnimal.utils.Constant;
import com.funnyAnimal.utils.DateUtil;
import com.funnyAnimal.utils.ImageLoadFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/6/16.
 */

public class ContentFragment extends BaseFragment {

    @BindView(R.id.img_daily)
    ImageView imgDaily;

    @BindView(R.id.day)
    TextView tvDay;

    @BindView(R.id.date)
    TextView tvDate;

    @BindView(R.id.text_daily)
    TextView tvDaily;

    @BindView(R.id.author)
    TextView tvAuthor;

    private Bundle bundle;

    public static ContentFragment newInstance(Bundle args) {
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ContentFragment() {
        super();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void afterViews() {
        bundle = getArguments();
        String date = bundle.getString("date");
        ImageLoadFactory.displayForHome(getContext(), bundle.getString("imgUrl").replace("https", "http"), imgDaily);
        tvDay.setText(DateUtil.getLastDay(date));
//        tvDate.setText(DateUtil.getWeekByDateStr(date) + "  " + DateUtil.getMonthByDateStr(date));
        tvDaily.setText(bundle.getString("text"));
        tvAuthor.setText(bundle.getString("author"));
    }

    @OnClick(R.id.img_daily)
    void onClick() {
        String metaUrl = bundle.getString("metaUrl");
        switch (bundle.getInt("type")) {
            case Constant.TYPEVIDEO:
                break;
            case Constant.TYPEHTML:
                startActivity(new Intent(getContext(), WebviewActivity.class).putExtra("metaUrl", metaUrl));
                break;
            case Constant.TYPETOPIC:
                break;
            case Constant.TYPEPIC:
                String[] urls = new String[1];
                urls[0] = bundle.getString("imgUrl").replace("https", "http");
                Bundle extras = new Bundle(2);
                extras.putStringArray(ImageGalleryActivity.IMAGES, urls);
                extras.putInt(ImageGalleryActivity.POSITION, 0);
                startActivity(new Intent(getContext(), ImageGalleryActivity.class).putExtras(extras));
                break;
            default:
                break;

        }
    }
}
