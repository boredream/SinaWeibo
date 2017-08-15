package com.boredream.weibo.net;

import com.boredream.bdcodehelper.lean.LcHttpRequest;

public class WbHttpRequest extends LcHttpRequest {

    private static volatile WbHttpRequest instance = null;

    public static WbHttpRequest getInstance() {
        if (instance == null) {
            synchronized (WbHttpRequest.class) {
                if (instance == null) {
                    instance = new WbHttpRequest();
                }
            }
        }
        return instance;
    }

    private WbHttpRequest() {
        super();
    }

    public WbApiService getApiService() {
        return retrofit.create(WbApiService.class);
    }

}
