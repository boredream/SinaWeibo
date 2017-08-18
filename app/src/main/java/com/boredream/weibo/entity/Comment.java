package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.Pointer;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/15
 *     desc   :
 * </pre>
 */
public class Comment extends Pointer {

    private User user;
    private String text;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
