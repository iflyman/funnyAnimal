package com.funnyAnimal.view.recycle;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.funnyAnimal.R;


/**
 * SimpleProgressView
 * @author brucewuu Created on 15/6/26.
 */
public class SimpleProgressView extends FrameLayout {

    private TextView textView;
    private View progressView;


    public SimpleProgressView(Context context) {
        this(context, null);
    }

    public SimpleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_simple_progress, this);
        textView = (TextView) findViewById(R.id.spv_text);
        progressView = findViewById(R.id.spv_progress_bar);
    }

    public void showProgress() {
        show();
        textView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    public void showText() {
        show();
        progressView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    public void showText(@StringRes int resId) {
        String text = getResources().getString(resId);
        showText(text);
    }

    public void showText(CharSequence text) {
        textView.setText(text);
        showText();
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }
}
