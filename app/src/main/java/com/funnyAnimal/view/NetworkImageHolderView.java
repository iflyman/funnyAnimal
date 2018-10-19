package com.funnyAnimal.view;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.funnyAnimal.R;
import com.funnyAnimal.utils.UiHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by bhyan on 2017/4/13.
 */

public class NetworkImageHolderView extends Holder<String> {

    private ImageView imageView;

    public NetworkImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = (ImageView) itemView.findViewById(R.id.ivPost);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void updateUI(String data) {
        ImageLoader.getInstance().displayImage(data, imageView, UiHelper.getHomeBackOption());
    }
}
