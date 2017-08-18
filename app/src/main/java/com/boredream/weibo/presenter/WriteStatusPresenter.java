package com.boredream.weibo.presenter;

import android.content.Context;

import com.boredream.bdcodehelper.lean.LcUtils;
import com.boredream.bdcodehelper.lean.entity.FileUploadResponse;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.CollectionUtils;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.weibo.constants.UserInfoKeeper;
import com.boredream.weibo.entity.Goods;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.net.RxComposer;
import com.boredream.weibo.net.WbHttpRequest;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WriteStatusPresenter implements WriteStatusContract.Presenter {

    private final WriteStatusContract.View view;

    public WriteStatusPresenter(WriteStatusContract.View view) {
        this.view = view;
    }

    @Override
    public void publishStatus(final Context context, final String text, List<String> paths) {
        final Map<String, Object> request = new HashMap<>();
        request.put("name", text);
        User currentUser = UserInfoKeeper.getInstance().getCurrentUser();
        request.put("user", LcUtils.getPointer(currentUser));
        Observable<Goods> observable = WbHttpRequest.getInstance().getApiService().statusesUpload(request);

        if(!CollectionUtils.isEmpty(paths)) {
            final int maxSize = DisplayUtils.dp2px(context, 300);
            List<Observable<FileUploadResponse>> observables = new ArrayList<>();
            for (final String path : paths) {
                // 每张图片先压缩后上传
                observables.add(Observable.create(new ObservableOnSubscribe<File>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<File> e) throws Exception {
                                File file = Glide.with(context)
                                        .download(path)
                                        .submit(maxSize, maxSize)
                                        .get();
                                e.onNext(file);
                            }
                        })
                        .flatMap(new Function<File, ObservableSource<FileUploadResponse>>() {
                            @Override
                            public ObservableSource<FileUploadResponse> apply(@NonNull File file) throws Exception {
                                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
                                String fileName = "image_" + System.currentTimeMillis();
                                return WbHttpRequest.getInstance().getApiService().fileUpload(fileName, requestBody);
                            }
                        }));
            }
            // 合并多个图片上传结果
            observable = Observable.zip(observables, new Function<Object[], String>() {
                        @Override
                        public String apply(@NonNull Object[] objects) throws Exception {
                            StringBuilder sb = new StringBuilder();
                            for (Object object : objects) {
                                if (object instanceof FileUploadResponse) {
                                    FileUploadResponse fur = (FileUploadResponse) object;
                                    sb.append("|").append(fur.getUrl());
                                }
                            }
                            return sb.substring(1);
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<Goods>>() {
                        @Override
                        public ObservableSource<Goods> apply(@NonNull String images) throws Exception {
                            request.put("image", images);
                            return WbHttpRequest.getInstance().getApiService().statusesUpload(request);
                        }
                    });
        }

        observable.compose(RxComposer.<Goods>commonProgress(view))
            .subscribe(new SimpleDisObserver<Goods>(){
                @Override
                public void onNext(@NonNull Goods goods) {
                    view.publishStatusSuccess(goods);
                }
            });
    }
}
