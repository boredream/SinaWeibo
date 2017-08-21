package com.boredream.weibo.net;

import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.lean.LcUtils;
import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.OkHttpClientFactory;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.MockUtils;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;
import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WbHttpRequestTest {

    private WbApiService apiService;

    private class TestHttpRequest {
        private static final String HOST = "https://api.leancloud.cn";
        private static final String APP_ID_NAME = "X-LC-Id";
        private static final String API_KEY_NAME = "X-LC-Key";
        private static final String SESSION_TOKEN_KEY = "X-LC-Session";

        private static final String APP_ID_VALUE = "fMdvuP11j3fRLxco6VQ9Aj69-gzGzoHsz";
        private static final String API_KEY_VALUE = "VuP45bVSUIpo4fLGEmJgfuba";

        protected Retrofit retrofit;

        protected TestHttpRequest() {
            List<Interceptor> interceptors = new ArrayList<>();
            interceptors.add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader(APP_ID_NAME, APP_ID_VALUE)
                            .addHeader(API_KEY_NAME, API_KEY_VALUE)
//                            .addHeader(SESSION_TOKEN_KEY, LcTokenKeeper.getInstance().getSessionToken())
                            .build();
                    return chain.proceed(request);
                }
            });
            interceptors.add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl url = request.url();
                    Set<String> names = url.queryParameterNames();
                    String urlStr = url.url().toString();

                    if(names.contains("page")) {
                        // 如果包含page，则自动转换为limit和skip的leancloud规则
                        int page = Integer.parseInt(url.queryParameterValues("page").get(0));
                        int skip = LcUtils.page2skip(page);

                        url = url.newBuilder()
                                .removeAllQueryParameters("page")
                                .addQueryParameter("limit", String.valueOf(LcUtils.PAGE_SIZE))
                                .addQueryParameter("skip", String.valueOf(skip))
                                .build();
                    }

                    if(urlStr.contains("Goods")) {
                        // 如果是Goods接口，则按时间倒序
                        url = url.newBuilder()
                                .addQueryParameter("order", "-createdAt")
                                .build();
                    }

                    request = chain.request().newBuilder()
                            .url(url)
                            .build();
                    return chain.proceed(request);
                }
            });

            // Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(HOST)
                    .addConverterFactory(GsonConverterFactory.create()) // gson
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava 响应式编程
                    .client(OkHttpClientFactory.get(interceptors))
                    .build();
        }
    }

    private class LogSubscriber<T> implements Observer<T> {

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(T o) {
            System.out.println("onNext " + o.toString());
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("onError " + t.toString());
        }

        @Override
        public void onComplete() {

        }
    }

    private BaseView view = new BaseView() {
        @Override
        public void showTip(String msg) {
            System.out.println("show tip " + msg);
        }

        @Override
        public void showProgress() {
            System.out.println("show progress");
        }

        @Override
        public void dismissProgress() {
            System.out.println("dismiss progress");
        }

        @Override
        public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
            return null;
        }
    };

    @Before
    public void init() {
        apiService = new TestHttpRequest().retrofit.create(WbApiService.class);
    }

    @Test
    public void test() {
        final int page = 1;
        Map<String, Object> request = new HashMap<>();
        request.put("page", page);
        request.put("include", "user");

        apiService.statusesHome(request)
                .compose(LcRxCompose.<Goods>handleListResponse())
                .compose(LcRxCompose.<ArrayList<Goods>>defaultFailed(view))
                .subscribe(new Observer<ArrayList<Goods>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Goods> goods) {
                        for (Goods good : goods) {
                            System.out.println(new Gson().toJson(good));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}