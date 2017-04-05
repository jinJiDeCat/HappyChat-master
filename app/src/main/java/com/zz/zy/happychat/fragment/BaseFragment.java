package com.zz.zy.happychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isInitView = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=View.inflate(getActivity(),getContentView(),null);
        ButterKnife.bind(this, view);
        initPresenter();
        initView(view);
        isInitView = true;
        lazyLoadData();
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //LogUtil.m("isVisibleToUser " + isVisibleToUser + "   " + this.getClass().getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
    private void lazyLoadData() {
        if (isFirstLoad) {
            //LogUtil.m("第一次加载 " + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        } else {
            //LogUtil.m("不是第一次加载" + " isInitView  " + isInitView + "  isVisible  " + isVisible + "   " + this.getClass().getSimpleName());
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            //LogUtil.m("不加载" + "   " + this.getClass().getSimpleName());
            return;
        }

        //LogUtil.m("完成数据第一次加载"+ "   " + this.getClass().getSimpleName());
        initData();
        isFirstLoad = false;
    }
    /**
     * 加载要显示的数据
     */
    protected abstract void initData();
    public abstract int getContentView();
    public abstract void initView(View view);
    public abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
