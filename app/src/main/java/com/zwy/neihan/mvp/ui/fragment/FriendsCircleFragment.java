package com.zwy.neihan.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.dialog.loading.OnCancelListener;
import com.zwy.neihan.di.component.DaggerFriendsCircleComponent;
import com.zwy.neihan.di.module.FriendsCircleModule;
import com.zwy.neihan.mvp.contract.FriendsCircleContract;
import com.zwy.neihan.mvp.presenter.FriendsCirclePresenter;

import com.zwy.neihan.R;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-27 3:53:47
 * 创建人:Alan
 * 文件描述：游戏-fragment
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class FriendsCircleFragment extends BaseFragment<FriendsCirclePresenter> implements FriendsCircleContract.View, OnCancelListener {


    public static FriendsCircleFragment newInstance() {
        FriendsCircleFragment fragment = new FriendsCircleFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerFriendsCircleComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .friendsCircleModule(new FriendsCircleModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_circle, container, false);
    }
    /**
     * 对用户可见并且view初始化完成时调用(该处加载数据只会加载一次，比如三个tab每个tab加载过一次就不会再次进入这个方法)
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    /**
     * 区别与initData(),该方法每次页面可见是均会加载数据 类似onResume()
     */
    @Override
    public void loadDataEveryTime() {

    }

    /**
     * 页面对用户不可见时(可选)
     */
    @Override
    public void stopLoadData() {

    }
    @Override
    public void showLoading() {
        ArmsUtils.showLoading("加载中...", true, this);
    }

    @Override
    public void hideLoading() {
        ArmsUtils.dissMissLoading();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.showToast(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    /**
     * 当进度条被取消时调用
     */
    @Override
    public void onCancel() {

    }
}
