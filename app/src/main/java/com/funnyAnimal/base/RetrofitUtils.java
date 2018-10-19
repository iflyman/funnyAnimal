package com.funnyAnimal.base;

import com.funnyAnimal.utils.UiHelper;
import com.funnyAnimal.utils.logger.L;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by bhyan on 2017/6/16.
 */

public class RetrofitUtils {

    private static final String BASE_URL = "http://39.107.234.66/funnyanimal/";
//    private static final String BASE_URL = "http://39.107.234.66:8082/funnyanimal/";
    private static final class SingletonHolder {
        static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    public static RetrofitUtils get() {
        return SingletonHolder.INSTANCE;
    }

    private Retrofit retrofit;

    private RetrofitUtils() {
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()));
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.readTimeout(20, TimeUnit.SECONDS);
        httpClient.writeTimeout(20, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (UiHelper.isJson(message) || UiHelper.isJsonArray(message)) {
                    L.json(message);
                } else {
                    L.d(message);
                }
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        retrofit = builder.client(httpClient.build()).build();
    }

    public <T> T create(Class<T> cls) {
        return retrofit.create(cls);
    }
}
