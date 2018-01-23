package com.zwy.neihan.app.impl;

import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * ================================================================
 * 创建时间：2017/9/4 下午3:44
 * 创建人：Alan
 * 文件描述：我能怎么办，我也很想怼她啊。
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public interface OnHttpRequestUseCache<T> {

    /**
     * 请求之前回调
     *
     * @param disposable 调用disposable.dispose();切断其关注的事件
     */
    void onStart(Disposable disposable);

    /**
     * 请求结束后调用(无论成功失败)
     */
    void onFinally();

    /**
     * 将view加入生命周期统一管理
     *
     * @return tips:RxUtils.bindToLifecycle(mRootView);
     */
    LifecycleTransformer<T> getCompose();

    /**
     * 全局异常捕获
     *
     * @return
     */
    RxErrorHandler getRxErrorHandler();

    /**
     * 网络请求成功的回调(缓存返回的数据需要自行处理)
     */
    void onSucc(Object t);


    /**
     * 网络请求失败的回调
     *
     * @param errMsg 导致失败的原因
     */
    void onError(String errMsg);
}
