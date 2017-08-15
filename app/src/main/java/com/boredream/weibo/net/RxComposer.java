package com.boredream.weibo.net;

import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.lean.net.LcRxCompose;
import com.boredream.bdcodehelper.net.CommonRxComposer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;

public class RxComposer {

   public static <T> ObservableTransformer<T, T> commonProgress(final BaseView view) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.compose(CommonRxComposer.<T>schedulers())
                        .compose(CommonRxComposer.<T>doProgress(view))
                        .compose(LcRxCompose.<T>defaultFailed(view));
            }
        };
    }

   public static <T> ObservableTransformer<T, T> commonRefresh(final BaseView view,
                                                               final boolean isLoadMore,
                                                               final SmartRefreshLayout refreshLayout) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.compose(CommonRxComposer.<T>schedulers())
                        .compose(LcRxCompose.<T>defaultFailed(view))
                        .doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                if(isLoadMore) refreshLayout.finishLoadmore();
                                else refreshLayout.finishRefresh();
                            }
                        });
            }
        };
    }

}
