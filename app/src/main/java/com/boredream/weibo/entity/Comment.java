package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.Pointer;

public class Comment extends Pointer {

    private Goods status;
    private User user;
    private String text;

    public Goods getStatus() {
        return status;
    }

    public void setStatus(Goods status) {
        this.status = status;
    }

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
