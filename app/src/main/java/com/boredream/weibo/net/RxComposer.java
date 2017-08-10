package com.boredream.weibo.net;

import com.boredream.bdcodehelper.base.BaseView;
import com.boredream.bdcodehelper.net.CommonRxComposer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * <pre>
 *     author : lichunyang
 *     time   : 2017/08/10
 *     desc   :
 * </pre>
 */
public class RxComposer {

   public static <T> ObservableTransformer<T, T> common(final BaseView view) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.compose(CommonRxComposer.<T>schedulers())
                        .compose(CommonRxComposer.<T>doProgress(view))
                        .compose(RxComposer.<T>defaultFailed(view));
            }
        };
    }

    private static <T> ObservableTransformer<T, T> defaultFailed(final BaseView view) {
        return new ObservableTransformer<T, T>(){
            @Override
            public ObservableSource<T> apply(@NonNull final Observable<T> upstream) {
                return upstream.doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        // TODO: 2017/8/10 更具体的处理
                        view.showTip(throwable.getLocalizedMessage());
                    }
                });
            }
        };
    }

}
