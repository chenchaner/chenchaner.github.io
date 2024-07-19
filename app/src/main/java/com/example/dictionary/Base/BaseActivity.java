package com.example.dictionary.Base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.R;

public abstract class BaseActivity <P extends BasePresenter,CONTRACT> extends AppCompatActivity implements View.OnClickListener, BaseView {
    public P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mPresenter = getmPresenter();
        mPresenter.bindView(this);
        initView();
        initData();
        initListener();
        initClass();
    }
    public abstract CONTRACT getContract();

    public abstract P getmPresenter();
    public abstract void initAdapter();
    public abstract int getContentViewID();
    public abstract void initClass();
    public abstract void initView();
    public abstract void initData();
    public abstract void initListener();
    public abstract <ERROR> void responseError(Error error,Throwable throwable);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unBindView();
    }
}
