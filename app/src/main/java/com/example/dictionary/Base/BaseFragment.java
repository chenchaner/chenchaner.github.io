package com.example.dictionary.Base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment <P extends BasePresenter,CONTRACT> extends Fragment implements View.OnClickListener,BaseView {
    public P mPresenter;
    private View rootView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPresenter = getmPresenter();
        mPresenter.bindView(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getContentViewID(),container,false);
        initView(rootView);
        initData();
        initListener();
        initClass();
        return rootView;
    }
    public abstract CONTRACT getContract();

    public abstract P getmPresenter();
    public abstract void initAdapter();
    public abstract int getContentViewID();
    public abstract void initClass();
    public abstract void initView(View rootView);
    public abstract void initData();
    public abstract void initListener();
    public abstract <ERROR> void responseError(Error error,Throwable throwable);

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unBindView();
    }
}
