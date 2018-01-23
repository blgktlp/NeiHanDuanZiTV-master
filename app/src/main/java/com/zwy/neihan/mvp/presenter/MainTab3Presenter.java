package com.zwy.neihan.mvp.presenter;

import android.app.Application;
import android.support.v4.app.Fragment;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.zwy.neihan.mvp.contract.MainTab3Contract;
import com.zwy.neihan.mvp.model.entity.HomeTabBean;
import com.zwy.neihan.mvp.ui.adapter.PageAdapter;
import com.zwy.neihan.mvp.ui.fragment.CityWideFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================================
 * 创建时间:2017-8-26 23:41:43
 * 创建人:Alan
 * 文件描述：xxxxxx控制器
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
@ActivityScope
public class MainTab3Presenter extends BasePresenter<MainTab3Contract.Model, MainTab3Contract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    @Inject
    public MainTab3Presenter(MainTab3Contract.Model model, MainTab3Contract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    public void getTabs() {
        mRootView.setAdapter(new PageAdapter(mRootView.getFMManager(), mModel.getFragments(), mModel.getTitle()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
