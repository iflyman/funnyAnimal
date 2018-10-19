package com.funnyAnimal.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyAnimal.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by bhyan on 2017/10/16.
 */

public class WebviewActivity extends SwipeBackActivity {

    @BindView(R.id.title_txt)
    TextView title_txt;

    @BindView(R.id.back_btn)
    ImageView back_btn;

    public WebView webView;
    private String url;
    private String title = null;

    @Override
    protected int getLayoutId() {
        return R.layout.act_webview;
    }

    @Override
    protected void afterViews() {
        Bundle extra = getIntent().getExtras();
        url = extra.getString("url");
        title = extra.getString("title");
        Log.e("bokey", "url:" + url);
        init();
        webView.loadUrl(url);
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        back_btn.setVisibility(View.VISIBLE);
        if (null != title)
            title_txt.setText(title);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);// 水平不显示
        webView.setVerticalScrollBarEnabled(true); // 垂直不显示
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.requestFocus();
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(false);
        // 网页自适应
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String childUrl) {
                view.loadUrl(childUrl);
                return true;
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            /**获取title**/
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (null != view.getTitle() && !view.getTitle().contains(".html"))
                    title_txt.setText(view.getTitle());
            }
        });

    }

    @OnClick(R.id.back_btn)
    void OnClick() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
