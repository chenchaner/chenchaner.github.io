package com.example.dictionary.Translation;

import android.util.Log;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.ITranslation;

public class TranslationPresenter extends BasePresenter<TranslationFragment,TranslationModel, ITranslation.VP> {

    @Override
    public TranslationModel getmModelInstance() {
        return new TranslationModel(this);
    }

    @Override
    public ITranslation.VP getContract() {
        return new ITranslation.VP() {
            @Override
            public void requestNetwork(String url, String inputText) {
                mModel.getContract().requestNetwork(url,inputText);
            }

            @Override
            public void responseForNetwork(String translation) {
                mView.getContract().responseForNetwork(translation);
                Log.d("debug", "responseForNetwork: "+translation);
            }
        };
    }
}
