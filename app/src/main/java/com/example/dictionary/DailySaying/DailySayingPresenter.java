package com.example.dictionary.DailySaying;

import android.util.Log;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IDailySaying;


public class DailySayingPresenter extends BasePresenter<DailySayingFragment,DailySayingModel, IDailySaying.VP> {

    @Override
    public DailySayingModel getmModelInstance() {
        return new DailySayingModel(this);
    }


    @Override
    public IDailySaying.VP getContract() {
        return new IDailySaying.VP() {
            @Override
            public void requestNetwork(String apiUrl) {
                mModel.getContract().requestNetwork(apiUrl);
            }

            @Override
            public void setImageUrl(String imageUrl) {
                mView.getContract().setImageUrl(imageUrl);
            }

            @Override
            public void setTextSentence(String TextSentence) {
                mView.getContract().setTextSentence(TextSentence);
            }

            @Override
            public void setTextTranslation(String TextTranslation) {
                mView.getContract().setTextTranslation(TextTranslation);
            }

            @Override
            public void setTextDate(String TextDate) {
                mView.getContract().setTextDate(TextDate);
                Log.d("P层", "setTextDate: "+TextDate);
            }

            @Override
            public void setTtsUrl(String ttsUrl) {
                mView.getContract().setTtsUrl(ttsUrl);
            }
        };
    }
}
