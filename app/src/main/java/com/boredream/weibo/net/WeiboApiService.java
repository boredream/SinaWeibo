package com.boredream.weibo.net;

import com.boredream.weibo.entity.Status;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.entity.response.CommentListResponse;
import com.boredream.weibo.entity.response.StatusListResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WeiboApiService {

    // 获取用户信息
    @GET("users/show.json")
    Observable<User> usersShow(
            @Query("uid") String uid);

    // 获取某个用户最新发表的微博列表
    @GET("statuses/user_timeline.json")
    Observable<StatusListResponse> statusesUser_timeline(
            @Query("uid") String uid,
            @Query("screen_name") String uname,
            @Query("page") int page);

    // 首页微博列表
    @GET("statuses/home_timeline.json")
    Observable<StatusListResponse> statusesHome_timeline(
            @Query("page") int page);

    // 微博评论列表
    @GET("comments/show.json")
    Observable<CommentListResponse> commentsShow(
            @Query("id") long statusId,
            @Query("page") int page);

    // 对一条微博进行评论
    @POST("comments/create.json")
    Observable<CommentListResponse> commentsCreate(
            @Body Map<String, String> request);

    // 转发一条微博
    @POST("statuses/repost.json")
    Observable<Status> statusesRepost(
            @Body Map<String, String> request);

    // 发布一条微博(带图片)
    @POST("statuses/upload.json")
    Observable<Status> statusesUpload(
            @Body Map<String, String> request);

    // 发布一条微博(不带图片)
    @POST("statuses/update.json")
    Observable<Status> statusesUpdate(
            @Body Map<String, String> request);

}
