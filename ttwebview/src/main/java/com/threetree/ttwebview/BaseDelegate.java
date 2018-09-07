package com.threetree.ttwebview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2018/7/23.
 */

public abstract class BaseDelegate extends Fragment {

    protected FragmentActivity mActivity;

    //子类必须实现,可以返回一个layout的资源id,或一个view
    public abstract Object setLayout();
    //子类初始化时回调
    public abstract void onBindView(@Nullable Bundle savedInstanceState, View rootView);

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = null;
        Object mLayout = setLayout();
        if (mLayout instanceof Integer) {
            rootView = inflater.inflate((Integer) mLayout, container, false);
        } else if (mLayout instanceof View) {
            rootView = (View) mLayout;
        }
        if (rootView != null) {
            onBindView(savedInstanceState, rootView);
        }
        return rootView;

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

    }
}
