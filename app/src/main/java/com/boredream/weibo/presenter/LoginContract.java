package com.boredream.weibo.presenter;


import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.weibo.entity.User;

public interface LoginContract {

    interface View extends BaseView {

        void loginSuccess(User user);

        void loginError();

    }

    interface Presenter extends BasePresenter {

        void login(String username, String password);

        void autoLogin(String sessionToken);

    }
}
