package com.funnyAnimal.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funnyAnimal.R;
import com.funnyAnimal.view.LProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by bhyan on 2017/6/16.
 */

public abstract class BaseFragment extends Fragment {

    @LayoutRes
    protected abstract int getLayoutId();

    @MainThread
    protected abstract void afterViews();

    protected final String TAG;

    private Unbinder unbinder;

    protected CompositeSubscription mSubscriptions;

    private Snackbar mSnackbar;

    protected LProgressDialog mProgressDialog;

    public BaseFragment() {
        Object o = this;
        this.TAG = o.getClass().getSimpleName();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        //L.ee(TAG + "--onCreateView-");
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterViews();
        //L.ee(TAG + "--onViewCreated-");
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(TAG);
        //L.ee(TAG + "--onResume-");
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(TAG);
        //L.ee(TAG + "--onPause-");
    }

    @Override
    public void onDestroyView() {
        if (null != mSubscriptions) {
            mSubscriptions.clear();
        }
        super.onDestroyView();
        unbinder.unbind();
        //L.ee(TAG + "--onDestroyView-");
    }

    @Override
    public void onDestroy() {
        if (null != mSubscriptions) {
            mSubscriptions.unsubscribe();
        }
        super.onDestroy();
    }

    protected void addSubscriptions(@NonNull final Subscription... subscriptions) {
        if (null == mSubscriptions) {
            mSubscriptions = new CompositeSubscription(subscriptions);
        } else {
            for (Subscription subscription : subscriptions) {
                mSubscriptions.add(subscription);
            }
        }
    }

    protected void showProgressDialog() {
        if (this.mProgressDialog == null)
            this.mProgressDialog = new LProgressDialog(getContext(), getString(R.string.plase_wait), true);

        this.mProgressDialog.show();
    }

    protected void showProgressDialog(String msg) {
        if (this.mProgressDialog == null)
            this.mProgressDialog = new LProgressDialog(getContext(), msg, true);

        if (!TextUtils.isEmpty(msg))
            this.mProgressDialog.setMessage(msg);
        this.mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (this.mProgressDialog != null)
            this.mProgressDialog.dismiss();
    }
}
