package com.zwy.neihan.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.boxing.impl.view.HackyViewPager;
import com.jess.arms.widget.dialog.loading.OnCancelListener;
import com.jess.arms.widget.tablayout.SlidingTabLayout;
import com.jess.arms.widget.tablayout.listener.OnTabSelectListener;
import com.zwy.neihan.R;
import com.zwy.neihan.app.EventBusTags;
import com.zwy.neihan.di.component.DaggerMainTab1Component;
import com.zwy.neihan.di.module.MainTab1Module;
import com.zwy.neihan.mvp.contract.MainTab1Contract;
import com.zwy.neihan.mvp.presenter.MainTab1Presenter;
import com.zwy.neihan.mvp.ui.adapter.PageAdapter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-26 23:40:51
 * 创建人:Alan
 * 文件描述：首页
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class MainTab1Fragment extends BaseFragment<MainTab1Presenter> implements MainTab1Contract.View, OnCancelListener, OnTabSelectListener {


    @BindView(R.id.tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_home)
    HackyViewPager mVp;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;

    public static MainTab1Fragment newInstance() {
        MainTab1Fragment fragment = new MainTab1Fragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTab1Component //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainTab1Module(new MainTab1Module(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab1, container, false);
    }

    /**
     * 对用户可见并且view初始化完成时调用(该处加载数据只会加载一次，比如三个tab每个tab加载过一次就不会再次进入这个方法)
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.getTabs();
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

    /**
     * 设置tabs数据
     *
     * @param pageAdapter
     */
    @Override
    public void setAdapter(PageAdapter pageAdapter) {
        mVp.setAdapter(pageAdapter);
        mTab.setViewPager(mVp, pageAdapter.getTitles());
        mVp.setCurrentItem(0);

        mTab.setOnTabSelectListener(this);
    }

    /**
     * 获取视图管理器
     *
     * @return
     */
    @Override
    public FragmentManager getFMManager() {
        return getActivity().getSupportFragmentManager();
    }

    @Override
    public void onTabSelect(int position) {
        mVp.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }

    @OnClick({R.id.iv_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh:
                EventBus.getDefault().post("", EventBusTags.HOME_TAB_REFRESH);
                break;
        }
    }
}
