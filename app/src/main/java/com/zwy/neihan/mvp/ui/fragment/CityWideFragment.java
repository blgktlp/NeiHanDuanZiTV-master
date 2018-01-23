package com.zwy.neihan.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.widget.dialog.loading.OnCancelListener;
import com.zwy.neihan.R;
import com.zwy.neihan.di.component.DaggerCityWideComponent;
import com.zwy.neihan.di.module.CityWideModule;
import com.zwy.neihan.mvp.contract.CityWideContract;
import com.zwy.neihan.mvp.model.entity.HomeTabBean;
import com.zwy.neihan.mvp.presenter.CityWidePresenter;

import butterknife.BindView;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================================
 * 创建时间:2017-8-27 3:52:22
 * 创建人:Alan
 * 文件描述：段友秀两个tab公用的 fragment            公用fmt   1 *  2
 * 看淡身边的虚伪，静心宁神做好自己。路那么长，无愧走好每一步。
 * ================================================================
 */
@SuppressLint("ValidFragment")
public class CityWideFragment extends BaseFragment<CityWidePresenter> implements CityWideContract.View, OnCancelListener {

    @BindView(R.id.tv)
    TextView mTv;
    private HomeTabBean mTabBean;

    public static CityWideFragment newInstance(HomeTabBean homeTabBean) {
        CityWideFragment fragment = new CityWideFragment(homeTabBean);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private CityWideFragment(HomeTabBean tabBean) {
        mTabBean = tabBean;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerCityWideComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cityWideModule(new CityWideModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_wide, container, false);
    }

    /**
     * 对用户可见并且view初始化完成时调用(该处加载数据只会加载一次，比如三个tab每个tab加载过一次就不会再次进入这个方法)
     *
     * @param savedInstanceState
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        mTv.setText(mTabBean.getName());
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
        mTabBean=null;
    }
}
