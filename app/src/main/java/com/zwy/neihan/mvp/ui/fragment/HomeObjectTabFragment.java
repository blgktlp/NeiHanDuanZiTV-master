package com.zwy.neihan.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.dialog.loading.OnCancelListener;
import com.zwy.neihan.R;
import com.zwy.neihan.app.EventBusTags;
import com.zwy.neihan.di.component.DaggerHomeObjectTabComponent;
import com.zwy.neihan.di.module.HomeObjectTabModule;
import com.zwy.neihan.mvp.contract.HomeObjectTabContract;
import com.zwy.neihan.mvp.model.entity.HomeTabBean;
import com.zwy.neihan.mvp.presenter.HomeObjectTabPresenter;
import com.zwy.neihan.mvp.ui.adapter.MainTab1Adapter;
import com.zwy.neihan.mvp.ui.widget.TipsView;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import timber.log.Timber;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-27 3:41:19
 * 创建人:Alan
 * 文件描述：1-5页小tab的容器-fragment    公用fmt   1 *  5
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
@SuppressLint("ValidFragment")
public class HomeObjectTabFragment extends BaseFragment<HomeObjectTabPresenter> implements HomeObjectTabContract.View, OnCancelListener {


    @BindView(R.id.rv_tab1_object)
    RecyclerView mRecyclerView;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    private HomeTabBean homeTabBean;
    private TipsView mTipsView;
    private Long lastTime = 0l;
    private AnimationDrawable mAnimationDrawable;

    public static HomeObjectTabFragment newInstance(HomeTabBean homeTabBean) {
        HomeObjectTabFragment fragment = new HomeObjectTabFragment(homeTabBean);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private HomeObjectTabFragment(HomeTabBean homeTabBean) {
        this.homeTabBean = homeTabBean;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerHomeObjectTabComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeObjectTabModule(new HomeObjectTabModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_object_tab, container, false);
    }


    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
    }

    /**
     * 对用户可见并且view初始化完成时调用(该处加载数据只会加载一次，比如三个tab每个tab加载过一次就不会再次进入这个方法)
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        mAnimationDrawable = ((AnimationDrawable) mIvLoading.getDrawable());
        initRecycleView();
        mPresenter.getData(homeTabBean, lastTime, false, 5, true, false);
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData(homeTabBean, lastTime, true, 30, true, false);
            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeTabBean = null;
        if (mTipsView != null) {
            mTipsView.destroy();
        }
        mTipsView = null;
    }


    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void setAdapter(MainTab1Adapter mainTab1Adapter) {
        mRecyclerView.setAdapter(mainTab1Adapter);
        mainTab1Adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getData(homeTabBean, lastTime, false, 30, false, true);
            }
        });
    }

    /**
     * 设置最后一次更新的时间
     *
     * @param lastTime
     */
    @Override
    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * 刷新后的红条通知
     *
     * @param msg
     * @param isPlaySound 是否播放声音
     */
    @Override
    public void showNewDataToast(String msg, boolean isPlaySound) {
        mTipsView = TipsView.init(new WeakReference<Context>(getActivity().getApplicationContext()).get(), msg, isPlaySound).showNewDataToast();
    }

    @Override
    public void startAnim() {
        mIvLoading.setVisibility(View.VISIBLE);
        mAnimationDrawable.start();
    }

    @Override
    public void stopAnim() {
        mAnimationDrawable.stop();
        mIvLoading.setVisibility(View.GONE);
    }

    /**
     * 设置刷新的状态
     *
     * @param isRefresh
     */
    @Override
    public void setRefreshState(boolean isRefresh) {
        mSw.setRefreshing(isRefresh);
    }

    @Override
    public int getEmptyView() {
        return R.layout.nulldataview;
    }

    @Subscriber(tag = EventBusTags.HOME_TAB_REFRESH, mode = ThreadMode.MAIN)
    public void onMainRefreshButtonClicked(String str) {
        if (this.getUserVisibleHint()) {
            Timber.d("刷新页面 - " + homeTabBean.getName());
            mPresenter.getData(homeTabBean, lastTime, true, 30, false, false);
        }
    }
}