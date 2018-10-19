package com.funnyAnimal.api;

import android.util.Log;

import com.funnyAnimal.activity.MyApplication;
import com.funnyAnimal.base.RetrofitUtils;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by 青峰 on 2017/7/22.
 */

public class HttpTokenExpireFunc implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount = 0;

    public HttpTokenExpireFunc(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (++retryCount <= maxRetries) {
                    QiniuErro qiniuErro = (QiniuErro) throwable;
                    if (qiniuErro.statusCode == -4 || qiniuErro.statusCode == -5) {
                        Log.e("bokey","token过期重新获取");
                        return RetrofitUtils.get().create(MainApi.class).getToken("").doOnNext(new Action1<Result<TokenResult>>() {
                            @Override
                            public void call(Result<TokenResult> tokenResultResult) {
                                Log.e("bokey","获得到的token"+tokenResultResult.data.token);
                                MyApplication.token = tokenResultResult.data.token;
                            }
                        });
                    }
                }
                return Observable.error(throwable);
            }
        });
    }
}
