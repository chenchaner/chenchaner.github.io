package com.example.dictionary.Recite;

import android.content.Context;
import android.util.Log;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IRecite;

import java.util.List;

public class RecitePresenter extends BasePresenter<ReciteActivity,ReciteModel, IRecite.VP> {
    @Override
    public ReciteModel getmModelInstance() {
        return new ReciteModel(this);
    }

    @Override
    public IRecite.VP getContract() {
        return new IRecite.VP() {
            @Override
            public void initList(Context context) {
                mModel.getContract().initList(context);
            }

            @Override
            public void responseInitList(List<String> wordList, List<String> pronunciationList, List<String> translationList, List<String> ttsUrlList) {
                Log.d("P层wordList", "initList: "+wordList);
                mView.getContract().responseInitList(wordList,pronunciationList,translationList,ttsUrlList);
            }
        };
    }
}
