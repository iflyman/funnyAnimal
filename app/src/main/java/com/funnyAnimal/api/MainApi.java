package com.funnyAnimal.api;

import android.support.v4.util.ArrayMap;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bhyan on 2017/6/16.
 */

public interface MainApi {

    @GET("v1/list")
    Observable<Result<List<DialyShow>>> queryDialyShows(@Query("appKey") String appKey);

    @POST("ally/login")
    Observable<Result<UserInfo>> qqLogin(@Body ArrayMap<String, String> params);

    @POST("user/register")
    Observable<Result<UserInfo>> userRegister(@Body ArrayMap<String, String> params);

    @POST("user/login")
    Observable<Result<UserInfo>> userLogin(@Body ArrayMap<String, String> params);

    @POST("user/update")
    Observable<Result<UserInfo>> userModify(@Body ArrayMap<String, String> params);

    @GET("v1/image/token")
    Observable<Result<TokenResult>> getToken(@Query("fileName") String fileName);

    @GET("v1/banner")
    Observable<Result<List<BannerEntity>>> getBanner();

    @POST("v1/say")
    Observable<Result> postContent(@Body ArrayMap<String, Object> params);

    @GET("v1/say/page")
    Observable<Result<List<TopicEntity>>> queryTopic(@Query("pageNumber") int page, @Query("pageSize") int pageSize, @Query("uid") String uid);

    @GET("v1/hotsay")
    Observable<Result<List<TopicEntity>>> queryHotTopic(@Query("pageNumber") int page, @Query("pageSize") int pageSize);

    @GET("v1/leaves")
    Observable<Result<TopicDetailEntity>> getTopicDetail(@Query("tid") String tid);

    @GET("v1/reply")
    Observable<Result<List<CommentEntity>>> getTopicComment(@Query("tid") String tid, @Query("pageNumber") int page, @Query("pageSize") int pageSize);

    @POST("v1/leave")
    Observable<Result> toComment(@Body ArrayMap<String, String> params);
}
