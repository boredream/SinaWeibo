package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.Pointer;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/15
 *     desc   :
 * </pre>
 */
public class User extends Pointer {

    private String username;
    private String nickname;
    private String password;
    private String avatarUrl;
    private String sessionToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
