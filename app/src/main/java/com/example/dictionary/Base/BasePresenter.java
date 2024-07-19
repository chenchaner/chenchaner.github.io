package com.example.dictionary.Base;

public abstract class BasePresenter <V extends BaseView,M extends BaseModel,CONTRACT> extends SuperBase <CONTRACT> {
    public V mView;
    public M mModel;
    public BasePresenter(){
        this.mModel = getmModelInstance();
    }
    public void bindView(V mView){
        this.mView = mView;
    }
    public void unBindView(){
        this.mView = null;
    }

    public abstract M getmModelInstance();
}
