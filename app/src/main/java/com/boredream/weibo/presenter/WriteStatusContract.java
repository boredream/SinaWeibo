package com.boredream.weibo.presenter;


import android.content.Context;

import com.boredream.bdcodehelper.base.BasePresenter;
import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.weibo.entity.Goods;

import java.io.File;
import java.util.List;

public interface WriteStatusContract {

    interface View extends BaseView {

        void publishStatusSuccess(Goods goods);
    }

    interface Presenter extends BasePresenter {

        void publishStatus(Context context, String text, List<String> paths);

    }
}
