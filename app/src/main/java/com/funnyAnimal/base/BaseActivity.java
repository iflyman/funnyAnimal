package com.funnyAnimal.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.funnyAnimal.R;
import com.funnyAnimal.view.LProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by bhyan on 2017/6/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int getLayoutId();

    @MainThread
    protected abstract void afterViews();

    private Unbinder unbinder;
    private CompositeSubscription mSubscriptions;
    protected LProgressDialog mProgressDialog;
    private Snackbar mSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        PushAgent.getInstance(this).onAppStart();
        unbinder = ButterKnife.bind(this);
        afterViews();
    }

    protected void setSupportToolbar(Toolbar toolbar) {
        if (null == toolbar)
            return;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void addSubscriptions(@NonNull final Subscription... subscriptions) {
        if (null == mSubscriptions) {
            mSubscriptions = new CompositeSubscription(subscriptions);
        } else {
            mSubscriptions.addAll(subscriptions);
        }
    }

    protected Snackbar showSnackbar(@NonNull View view, @NonNull CharSequence text, int duration, View.OnClickListener action) {
        mSnackbar = Snackbar.make(view, text, duration);
        if (null != action)
            mSnackbar.setAction(R.string.re_try, action);
        mSnackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                mSnackbar = null;
            }
        });
        mSnackbar.show();
        return mSnackbar;
    }

    protected void dismissSnackbar() {
        if (null != mSnackbar && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }

    protected void showProgressDialog() {
        if (this.mProgressDialog == null)
            this.mProgressDialog = new LProgressDialog(this, getString(R.string.plase_wait), true);

        this.mProgressDialog.show();
    }

    protected void showProgressDialog(String msg) {
        if (this.mProgressDialog == null)
            this.mProgressDialog = new LProgressDialog(this, msg, true);

        if (!TextUtils.isEmpty(msg))
            this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (this.mProgressDialog != null)
            this.mProgressDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        //新架构
        if (null != mSubscriptions) {
            mSubscriptions.unsubscribe();
            mSubscriptions = null;
        }
        unbinder.unbind();
        super.onDestroy();
    }
}
