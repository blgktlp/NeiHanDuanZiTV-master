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
import com.zwy.neihan.di.component.DaggerMainTab2Component;
import com.zwy.neihan.di.module.MainTab2Module;
import com.zwy.neihan.mvp.contract.MainTab2Contract;
import com.zwy.neihan.mvp.presenter.MainTab2Presenter;
import com.zwy.neihan.mvp.ui.adapter.PageAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-26 23:41:21
 * 创建人:Alan
 * 文件描述：段友秀视图页面-fragment
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
public class MainTab2Fragment extends BaseFragment<MainTab2Presenter> implements MainTab2Contract.View, OnCancelListener, OnTabSelectListener {


    @BindView(R.id.tab_tabs_2)
    SlidingTabLayout mTab2;
    @BindView(R.id.vp_home_2)
    HackyViewPager mVpHome2;
    @BindView(R.id.iv_refresh_2)
    ImageView mIvRefresh2;
    @BindView(R.id.iv_submit)
    ImageView mIvSubmit;

    public static MainTab2Fragment newInstance() {
        MainTab2Fragment fragment = new MainTab2Fragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMainTab2Component //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainTab2Module(new MainTab2Module(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab2, container, false);
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

    @OnClick({R.id.iv_refresh_2, R.id.iv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh_2:
                showMessage("刷新段友秀");
                break;
            case R.id.iv_submit:
                showMessage("录像页面");
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
        mVpHome2.setAdapter(pageAdapter);
        mTab2.setViewPager(mVpHome2, pageAdapter.getTitles());
        mVpHome2.setCurrentItem(0);
        mTab2.setOnTabSelectListener(this);
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
        mVpHome2.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }
}
