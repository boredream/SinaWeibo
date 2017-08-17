package com.boredream.weibo.net;

import com.boredream.bdcodehelper.lean.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.lean.entity.FileUploadResponse;
import com.boredream.bdcodehelper.lean.entity.LeanCloudObject;
import com.boredream.bdcodehelper.lean.entity.ListResponse;
import com.boredream.bdcodehelper.lean.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.lean.entity.UserRegisterByMobilePhone;
import com.boredream.weibo.entity.Comment;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

// FIXME: 2017/8/15 常见api service怎么处理更好
public interface WbApiService {
    // 登录用户
    @GET("/1.1/login")
    Observable<User> login(
            @Query("username") String username,
            @Query("password") String password);

    // 注册
    @POST("/1.1/users")
    Observable<User> userRegist(
            @Body User user);

    // 利用token获取登陆用户信息
    @GET("/1.1/users/me")
    Observable<User> login();

    // 手机获取验证码
    @POST("/1/requestSmsCode")
    Observable<Object> requestSmsCode(
            @Body Map<String, Object> mobilePhoneNumber);

    // 手机号注册
    @POST("1.1/usersByMobilePhone")
    Observable<User> usersByMobilePhone(
            @Body UserRegisterByMobilePhone user);

    // 忘记密码重置
    @PUT("/1/resetPasswordBySmsCode/{smsCode}")
    Observable<Object> resetPasswordBySmsCode(
            @Path("smsCode") String smsCode,
            @Body Map<String, Object> password);

    // 旧密码修改新密码
    @POST("/1/updateUserPassword/{objectId}")
    Observable<User> updateUserPassword(
            @Path("smsCode") String smsCode,
            @Body UpdatePswRequest updatePswRequest);

    // 获取用户详情
    @GET("/1/users/{objectId}")
    Observable<User> getUserById(
            @Path("objectId") String objectId);

    // 根据条件获取用户集合
    @GET("/1.1/users")
    Observable<ListResponse<User>> getUsersByWhere(
            @Query("limit") int perPageCount,
            @Query("skip") int page,
            @Query("where") String where);

    // 根据条件获取用户集合
    @GET("/1.1/users")
    Observable<ListResponse<User>> getUsersByWhere(
            @Query("where") String where);

    // 修改用户详情(注意, 提交什么参数修改什么参数)
    @PUT("/1/users/{objectId}")
    Observable<LeanCloudObject> updateUserById(
            @Path("objectId") String userId,
            @Body Map<String, Object> updateInfo);

    // 上传图片接口
    @POST("/1/files/{fileName}")
    Observable<FileUploadResponse> fileUpload(
            @Path("fileName") String fileName,
            @Body RequestBody image);

    // 查询app更新信息
    @GET("/1/classes/AppUpdateInfo")
    Observable<AppUpdateInfo> getAppUpdateInfo();

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);




    
    // 获取某个用户最新发表的微博列表
    @GET("1/classes/Goods")
    Observable<ArrayList<Goods>> statusesUser_timeline(
            @Query("uid") String uid,
            @Query("screen_name") String uname,
            @Query("page") int page);

    // 首页微博列表
    @GET("1/classes/Goods")
    Observable<ListResponse<Goods>> statusesHome(
            @Query("page") int page);

    // 发布一条微博
    @POST("1/classes/Goods")
    Observable<Goods> statusesUpload(
            @Body Map<String, String> request);

    // 微博评论列表
    @GET("comments/show.json")
    Observable<ArrayList<Comment>> commentsShow(
            @Query("id") long statusId,
            @Query("page") int page);

    // 对一条微博进行评论
    @POST("comments/create.json")
    Observable<Comment> commentsCreate(
            @Body Map<String, String> request);

    // 转发一条微博
    @POST("statuses/repost.json")
    Observable<Goods> statusesRepost(
            @Body Map<String, String> request);

}
