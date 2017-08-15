package com.boredream.weibo.net;

import com.boredream.bdcodehelper.base.AppKeeper;
import com.boredream.bdcodehelper.utils.MockUtils;
import com.boredream.weibo.entity.User;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class WbHttpRequestTest {

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

    @Test
    public void testRegister() {
        User user = new User();
        user.setUsername("18551681236");
        user.setPassword("123456");
        user.setNickname("我的老李");
        user.setAvatarUrl(MockUtils.getImgUrl());

        WbHttpRequest.getInstance()
                .getApiService()
                .userRegist(user)
                .subscribe(new LogSubscriber<User>());
    }

}