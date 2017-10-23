package com.boredream.weibo.entity;

import com.boredream.bdcodehelper.lean.entity.Pointer;

public class HotPatchInfo extends Pointer {

    private int apkPatchVersion;
    /**
     * 热更新补丁版本号，格式1.0
     */
    private String hotPatchVersion;
    private String hotPatchFileUrl;

    public int getApkPatchVersion() {
        return apkPatchVersion;
    }

    public void setApkPatchVersion(int apkPatchVersion) {
        this.apkPatchVersion = apkPatchVersion;
    }

    public String getHotPatchVersion() {
        return hotPatchVersion;
    }

    public void setHotPatchVersion(String hotPatchVersion) {
        this.hotPatchVersion = hotPatchVersion;
    }

    public String getHotPatchFileUrl() {
        return hotPatchFileUrl;
    }

    public void setHotPatchFileUrl(String hotPatchFileUrl) {
        this.hotPatchFileUrl = hotPatchFileUrl;
    }

    @Override
    public String toString() {
        return "HotPatchInfo{" +
                "apkPatchVersion=" + apkPatchVersion +
                ", hotPatchVersion='" + hotPatchVersion + '\'' +
                ", hotPatchFileUrl='" + hotPatchFileUrl + '\'' +
                '}';
    }
}
