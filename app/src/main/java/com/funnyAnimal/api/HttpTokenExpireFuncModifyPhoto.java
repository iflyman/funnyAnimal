package com.funnyAnimal.api;

import android.util.Log;

import com.funnyAnimal.activity.MyApplication;
import com.funnyAnimal.base.RetrofitUtils;
import com.funnyAnimal.utils.PreferenceUtil;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by 青峰 on 2017/8/26.
 */

public class HttpTokenExpireFuncModifyPhoto implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount = 0;

    public HttpTokenExpireFuncModifyPhoto(int maxRetries, int retryDelayMillis) {
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
                        return RetrofitUtils.get().create(MainApi.class).getToken(PreferenceUtil.getUserUuid() + ".png").doOnNext(new Action1<Result<TokenResult>>() {
                            @Override
                            public void call(Result<TokenResult> tokenResultResult) {
                                Log.e("bokey","获得到的Modifytoken"+tokenResultResult.data.token);
                                MyApplication.modifyToken = tokenResultResult.data.token;
                            }
                        });
                    }
                }
                return Observable.error(throwable);
            }
        });
    }
}
