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
import com.zwy.neihan.di.component.DaggerMainTab3Component;
import com.zwy.neihan.di.module.MainTab3Module;
import com.zwy.neihan.mvp.contract.MainTab3Contract;
import com.zwy.neihan.mvp.presenter.MainTab3Presenter;
import com.zwy.neihan.mvp.ui.adapter.PageAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-26 23:41:43
 * 创建人:Alan
 * 文件描述：发现视图页面-fragment
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class MainTab3Fragment extends BaseFragment<MainTab3Presenter> implements MainTab3Contract.View, OnCancelListener, OnTabSelectListener {


    @BindView(R.id.tab_tabs_3)
    SlidingTabLayout mTabTabs3;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.vp_home_3)
    HackyViewPager mVpHome3;
    @BindView(R.id.iv_banner)
    ImageView mIvBanner;

    public static MainTab3Fragment newInstance() {
        MainTab3Fragment fragment = new MainTab3Fragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTab3Component //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainTab3Module(new MainTab3Module(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab3, container, false);
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

    @OnClick({R.id.iv_location, R.id.iv_search, R.id.iv_banner})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                showMessage("附近的段友");
                break;
            case R.id.iv_search:
                showMessage("搜索段子");
                break;
            case R.id.iv_banner:
                showMessage("广告");
                break;
        }
    }

    /**
     * 设置tabs数据
     *
     * @param pageAdapter
     */
    @Override
    public void setAdapter(PageAdapter pageAdapter) {
        mVpHome3.setAdapter(pageAdapter);
        mTabTabs3.setViewPager(mVpHome3, pageAdapter.getTitles());
        mVpHome3.setCurrentItem(0);
        mTabTabs3.setOnTabSelectListener(this);
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
        mVpHome3.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }
}
