package com.boredream.weibo.net;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.boredream.weibo.BaseApplication;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeiboHttpRequest {

    private static final String HOST = "https://api.weibo.com/2/";

    private Retrofit retrofit;
    private static volatile WeiboHttpRequest singleton = null;

    public static WeiboHttpRequest getSingleton() {
        if (singleton == null) {
            synchronized (WeiboHttpRequest.class) {
                if (singleton == null) {
                    singleton = new WeiboHttpRequest();
                }
            }
        }
        return singleton;
    }

    private WeiboHttpRequest() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        // headers
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        // 统一添加token
                        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(BaseApplication.app);
                        Request request = chain.request();

                        if (request.method().equals("GET")) {
                            HttpUrl url = request.url()
                                    .newBuilder()
                                    .addQueryParameter("access_token", token.getToken())
                                    .build();

                            request = request.newBuilder()
                                    .url(url)
                                    .build();
                        }

                        return chain.proceed(request);
                    }
                })
                .build();

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public WeiboApiService getApiService() {
        return retrofit.create(WeiboApiService.class);
    }

}

