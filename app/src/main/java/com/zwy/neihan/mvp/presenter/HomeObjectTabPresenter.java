package com.zwy.neihan.mvp.presenter;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.zwy.neihan.app.GlobalConfiguration;
import com.zwy.neihan.app.impl.OnHttpRequestUseCache;
import com.zwy.neihan.app.utils.RxUtils;
import com.zwy.neihan.mvp.contract.HomeObjectTabContract;
import com.zwy.neihan.mvp.model.entity.BaseJson;
import com.zwy.neihan.mvp.model.entity.HomeTabBean;
import com.zwy.neihan.mvp.model.entity.NeiHanContentBean;
import com.zwy.neihan.mvp.ui.adapter.MainTab1Adapter;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


/**
 * ================================================================
 * 创建时间:2017-8-27 3:41:19
 * 创建人:Alan
 * 文件描述：首页上部1-5个tab的内容容器
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
@ActivityScope
public class HomeObjectTabPresenter extends BasePresenter<HomeObjectTabContract.Model, HomeObjectTabContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private MainTab1Adapter mainTab1Adapter;
//    private int retryCount = 30;

    @Inject
    public HomeObjectTabPresenter(HomeObjectTabContract.Model model, HomeObjectTabContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }


    public synchronized void getData(HomeTabBean homeTabBean, Long lastTime, boolean isUpData, int count, boolean isShowLoading, boolean isAdd) {
        if (mainTab1Adapter == null) {
            mainTab1Adapter = new MainTab1Adapter(null);
            mainTab1Adapter.openLoadAnimation(GlobalConfiguration.ListAnim);
            mRootView.setAdapter(mainTab1Adapter);
        }

        RxUtils.doHttpRequest(mModel.getMainTab1ObjectData(String.valueOf(homeTabBean.getList_id()), lastTime, count, isUpData || isAdd),
                new OnHttpRequestUseCache<BaseJson<NeiHanContentBean>>() {
                    @Override
                    public void onStart(Disposable disposable) {
                        addDispose(disposable);
                        if (isShowLoading || isUpData)
                            mRootView.setRefreshState(true);
//                        mRootView.startAnim();
//                            mRootView.showLoading();
//                        if (isUpData){
//
//                        }

                    }

                    @Override
                    public void onFinally() {
                        if (isShowLoading || isUpData)
                            mRootView.setRefreshState(false);
                        mRootView.stopAnim();
//                            mRootView.hideLoading();
//                        if (isUpData){
//
//                        }
                    }

                    @Override
                    public LifecycleTransformer<BaseJson<NeiHanContentBean>> getCompose() {
                        return RxUtils.bindToLifecycle(mRootView);
                    }

                    @Override
                    public RxErrorHandler getRxErrorHandler() {
                        return mErrorHandler;
                    }

                    @Override
                    public void onSucc(Object object) {
                        NeiHanContentBean neiHanContentBean = null;
                        String m_tip = "";
                        if (object instanceof LinkedTreeMap) {
                            String neihanStr = new Gson().toJson(object);
                            neiHanContentBean = new Gson().fromJson(neihanStr, new TypeToken<NeiHanContentBean>() {
                            }.getType());
                            Timber.d("数据来源-缓存");
                            m_tip = "数据来自缓存";
                        } else {
                            neiHanContentBean = (NeiHanContentBean) object;
                            Timber.d("数据来源-网络获取");
                            m_tip = neiHanContentBean.getTip();
                            /*数据接口来自第三方，无法确定做分页的ID，故在此做模拟没有更多信息*/
                        }
                        mRootView.showNewDataToast(m_tip, true);
                            /*刷新数据时 重新设置适配器数据*/
                        if (isUpData) {
                            mainTab1Adapter.setNewData(neiHanContentBean.getData());
                        } else {
                            synchronized (neiHanContentBean) {
                                for (NeiHanContentBean.DataBean dataBean : neiHanContentBean.getData()) {
                                    mainTab1Adapter.addData(dataBean);
                                }
                            }
                            if (mainTab1Adapter.isLoading()) {
                                mainTab1Adapter.loadMoreComplete();
                            }
                        }
                        mRootView.setLastTime((long) neiHanContentBean.getMin_time());
                        mainTab1Adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String errMsg) {
                        ArmsUtils.showToast(errMsg);
                        if (mainTab1Adapter.isLoading()) {
                            mainTab1Adapter.loadMoreFail();
                        }
                    }
                });
//        mModel.getMainTab1ObjectData(String.valueOf(homeTabBean.getList_id()), lastTime, count, isUpData)
//                .subscribeOn(Schedulers.io())
//                .retryWhen(new RetryWithDelay(NeiHanConfig.NETWORK_RETRY_TIMES, NeiHanConfig.NETWORK_RETRY_DELAYSECOND))
//                .doOnSubscribe(disposable ->
//                        {
//                            if (isShowLoading)
//                                mRootView.showLoading();
//                        }
//                )
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() ->
//                        {
//                            if (isShowLoading)
//                                mRootView.hideLoading();
//                        }
//
//                )
//                .compose(RxUtils.bindToLifecycle(mRootView))
////                .flatMap(new Function<BaseJson<NeiHanContentBean>, ObservableSource<BaseJson<NeiHanContentBean>>>() {
////                    @Override
////                    public ObservableSource<BaseJson<NeiHanContentBean>> apply(@NonNull BaseJson<NeiHanContentBean> neiHanContentBeanBaseJson) throws Exception {
////                        if (neiHanContentBeanBaseJson.getCode().equals("retry")) {
////                            return Observable.error(new MyRetryException());
////                        }
////                        return Observable.just(neiHanContentBeanBaseJson);
////                    }
////                })
//                .subscribe(new ErrorHandleSubscriber<BaseJson<NeiHanContentBean>>(mErrorHandler) {
//                    @Override
//                    public void onNext(@NonNull BaseJson<NeiHanContentBean> data) {
//                        Timber.e(data + "");
//
//                    }
////
////                    @Override
////                    public void onError(@NonNull Throwable e) {
////                        super.onError(e);
////                        if (e instanceof MyRetryException) {
////                            Timber.e("正在重试....");
//////                            if (retryCount > 0) {
////                                getData(homeTabBean, lastTime, isUpData, count,true);
//////                            }
//////                            retryCount--;
////                        }
////                    }
//                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
        this.mainTab1Adapter = null;
    }


}
