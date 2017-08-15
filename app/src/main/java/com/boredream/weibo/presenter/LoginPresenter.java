package com.boredream.weibo.presenter;

import android.os.SystemClock;

import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.lean.net.LcTokenKeeper;
import com.boredream.bdcodehelper.net.CommonRxComposer;
import com.boredream.bdcodehelper.net.SimpleDisObserver;
import com.boredream.bdcodehelper.utils.LogUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.weibo.constants.UserInfoKeeper;
import com.boredream.weibo.entity.User;
import com.boredream.weibo.net.WbHttpRequest;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class LoginPresenter implements LoginContract.Presenter {

    private static final int SPLASH_DUR_MIN_TIME = 1500;
    private static final int SPLASH_DUR_MAX_TIME = 5000;

    private final LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void autoLogin(String sessionToken) {
        final long startTime = SystemClock.elapsedRealtime();
        decObservable(WbHttpRequest.getInstance()
                .getApiService()
                .login()
                .flatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull User response) throws Exception {
                        // 接口返回后，凑够最短时间跳转
                        long requestTime = SystemClock.elapsedRealtime() - startTime;
                        long delayTime = requestTime < SPLASH_DUR_MIN_TIME
                                ? SPLASH_DUR_MIN_TIME - requestTime : 0;
                        LogUtils.showLog("autoLogin delay = " + delayTime);
                        return Observable.just(response).delay(delayTime, TimeUnit.MILLISECONDS);
                    }
                })
                .timeout(SPLASH_DUR_MAX_TIME, TimeUnit.MILLISECONDS));
    }

    @Override
    public void login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            view.showTip("用户名不能为空");
            return;
        }

        if (StringUtils.isEmpty(password)) {
            view.showTip("密码不能为空");
            return;
        }

        decObservable(WbHttpRequest.getInstance()
                .getApiService()
                .login(username, password));
    }

    private void decObservable(Observable<User> observable) {
        observable.compose(CommonRxComposer.<User>schedulers())
                .compose(LcRxCompose.<User>defaultFailed(view))
                .subscribe(new SimpleDisObserver<User>() {
                    @Override
                    public void onNext(@NonNull User user) {
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                        // 保存自动登录使用的信息
                        LcTokenKeeper.getInstance().saveSessionToken(user.getSessionToken());

                        view.loginSuccess(user);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.loginError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
