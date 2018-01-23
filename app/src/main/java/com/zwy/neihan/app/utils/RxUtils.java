package com.zwy.neihan.app.utils;

import com.jess.arms.mvp.IView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zwy.neihan.NeiHanConfig;
import com.zwy.neihan.app.impl.OnHttpRequest;
import com.zwy.neihan.app.impl.OnHttpRequestUseCache;
import com.zwy.neihan.mvp.model.entity.BaseJson;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

/**
 * Created by jess on 11/10/2016 16:39
 * Contact with jess.yan.effort@gmail.com
 */

public class RxUtils {

    private RxUtils() {

    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                view.showLoading();//显示进度条
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action() {
                            @Override
                            public void run() {
                                view.hideLoading();//隐藏进度条
                            }
                        }).compose(RxUtils.bindToLifecycle(view));
            }
        };
    }


    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof LifecycleProvider){
            return ((LifecycleProvider)view).bindToLifecycle();
        }else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }

    }


    public static <W, F> void doHttpRequest(Observable<W> observable, OnHttpRequest<W, F> onHttpRequest) {
        if (onHttpRequest == null || observable == null) return;
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(NeiHanConfig.NETWORK_RETRY_TIMES, NeiHanConfig.NETWORK_RETRY_DELAYSECOND))
                .doOnSubscribe(disposable -> onHttpRequest.onStart(disposable)
                ).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> onHttpRequest.onFinally())
                .compose(onHttpRequest.getCompose())
                .subscribe(new ErrorHandleSubscriber<W>(onHttpRequest.getRxErrorHandler()) {
                    @Override
                    public void onNext(@NonNull W w) {
                        if (w instanceof BaseJson) {
                            BaseJson baseJson = (BaseJson) w;
                            if (!baseJson.isSuccess()) {
                                onHttpRequest.onError(baseJson.getMsg());
                                return;
                            }
                            onHttpRequest.onSucc((F) baseJson.getData());

                        } else {
                            onHttpRequest.onError("未知的数据类型");
                        }
                    }
                });
    }

    public static <W, F> void doHttpRequest(Observable<W> observable, OnHttpRequestUseCache<W> onHttpRequest) {
        if (onHttpRequest == null || observable == null) return;
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(NeiHanConfig.NETWORK_RETRY_TIMES, NeiHanConfig.NETWORK_RETRY_DELAYSECOND))
                .doOnSubscribe(disposable -> onHttpRequest.onStart(disposable)
                ).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> onHttpRequest.onFinally())
                .compose(onHttpRequest.getCompose())
                .subscribe(new ErrorHandleSubscriber<W>(onHttpRequest.getRxErrorHandler()) {
                    @Override
                    public void onNext(@NonNull W w) {
                        if (w instanceof BaseJson) {
                            BaseJson baseJson = (BaseJson) w;
                            if (!baseJson.isSuccess()) {
                                onHttpRequest.onError(baseJson.getMsg());
                                return;
                            }

                            onHttpRequest.onSucc(baseJson.getData());

                        } else {
                            onHttpRequest.onError("未知的数据类型");
                        }
                    }
                });
    }

}
