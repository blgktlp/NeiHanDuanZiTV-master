package com.zwy.neihan.mvp.model;

import android.app.Application;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.jess.arms.widget.tablayout.bean.TabEntity;
import com.jess.arms.widget.tablayout.listener.CustomTabEntity;
import com.zwy.neihan.R;
import com.zwy.neihan.mvp.contract.MainContract;
import com.zwy.neihan.mvp.ui.fragment.MainTab1Fragment;
import com.zwy.neihan.mvp.ui.fragment.MainTab2Fragment;
import com.zwy.neihan.mvp.ui.fragment.MainTab3Fragment;
import com.zwy.neihan.mvp.ui.fragment.MainTab4Fragment;

import java.util.ArrayList;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {
    private Gson mGson;
    private Application mApplication;
    private String[] mTitles = {"首页", "段友秀", "","发现", "我的"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    @Inject
    public MainModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
        super(repositoryManager);
        this.mGson = gson;
        this.mApplication = application;
        mFragments.add(MainTab1Fragment.newInstance());
        mFragments.add(MainTab2Fragment.newInstance());
        mFragments.add(new Fragment());
        mFragments.add(MainTab3Fragment.newInstance());
        mFragments.add(MainTab4Fragment.newInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
        this.mTitles=null;
        this.mFragments=null;
    }

    /**
     * 获取tab页填充的数据
     *
     * @return
     */
    @Override
    public ArrayList<CustomTabEntity> getmTabEntities() {



        int[] mIconUnselectIds = {
                R.mipmap.ic_tab_home_normal, R.mipmap.ic_tab_discovery_normal,R.drawable.centerbtselector,
                R.mipmap.ic_tab_audit_normal, R.mipmap.ic_tab_msg_normal};
        int[] mIconSelectIds = {
                R.mipmap.ic_tab_home_pressed, R.mipmap.ic_tab_discovery_pressed,R.drawable.centerbtselector,
                R.mipmap.ic_tab_audit_pressed, R.mipmap.ic_tab_msg_pressed};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < mFragments.size(); i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        return mTabEntities;
    }

    /**
     * 获取标题
     *
     * @return
     */
    @Override
    public String[] getTitles() {
        return mTitles;
    }

    /**
     * 获取单页对应的f
     *
     * @return
     */
    @Override
    public ArrayList<Fragment> getFragments() {
        return mFragments;
    }

}