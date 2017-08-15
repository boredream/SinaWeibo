package com.boredream.weibo.constants;

import com.boredream.bdcodehelper.base.BaseUserInfoKeeper;
import com.boredream.weibo.activity.LoginActivity;
import com.boredream.weibo.entity.User;

public class UserInfoKeeper extends BaseUserInfoKeeper<User, LoginActivity> {

    private static volatile UserInfoKeeper instance = null;

    public static UserInfoKeeper getInstance() {
        if (instance == null) {
            synchronized (UserInfoKeeper.class) {
                if (instance == null) {
                    instance = new UserInfoKeeper();
                }
            }
        }
        return instance;
    }

    private UserInfoKeeper() {
        // private
    }

}
