package com.example.dictionary.ReciteBook;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IReciteBook;

public class ReciteBookPresenter extends BasePresenter<ReciteBookActivity,ReciteBookModel, IReciteBook.VP> {
    @Override
    public ReciteBookModel getmModelInstance() {
        return new ReciteBookModel(this);
    }

    @Override
    public IReciteBook.VP getContract() {
        return new IReciteBook.VP() {
            @Override
            public void requestNetworkData(String apiUrl, int number) {
                mModel.getContract().requestNetworkData(apiUrl,number);
            }

            @Override
            public void responseForFetch(String word, String symbol, String desc, String soundUrl) {
                mView.getContract().responseForFetch(word, symbol, desc, soundUrl);
            }
        };
    }
}
