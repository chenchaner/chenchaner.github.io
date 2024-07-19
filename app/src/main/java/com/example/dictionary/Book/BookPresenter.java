package com.example.dictionary.Book;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IBook;
import com.example.dictionary.RecyclerView.Book;

import java.util.List;

public class BookPresenter extends BasePresenter<BookActivity,BookModel, IBook.VP> {
    @Override
    public BookModel getmModelInstance() {
        return new BookModel(this);
    }

    @Override
    public IBook.VP getContract() {
        return new IBook.VP() {
            @Override
            public void fetchBookInformation(String apiUrl) {
                mModel.getContract().fetchBookInformation(apiUrl);
            }

            @Override
            public void responseFetch(List<Book> bookList) {
                mView.getContract().responseFetch(bookList);
            }
        };
    }
}
