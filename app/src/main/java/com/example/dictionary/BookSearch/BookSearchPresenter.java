package com.example.dictionary.BookSearch;

import android.content.Context;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IBookSearch;

public class BookSearchPresenter extends BasePresenter<BookSearchFragment,BookSearchModel, IBookSearch.VP> {
    @Override
    public BookSearchModel getmModelInstance() {
        return new BookSearchModel(this);
    }

    @Override
    public IBookSearch.VP getContract() {
        return new IBookSearch.VP() {
            @Override
            public void fetchBookNumber(Context context) {
                mModel.getContract().fetchBookNumber(context);
            }

            @Override
            public void responseBookNumber(String number) {
                mView.getContract().responseBookNumber(number);
            }
        };
    }
}
