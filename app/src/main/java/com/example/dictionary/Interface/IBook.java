package com.example.dictionary.Interface;

import com.example.dictionary.RecyclerView.Book;

import java.util.List;

public interface IBook {
    public interface VP{
        public void fetchBookInformation(String apiUrl);
        public void responseFetch(List<Book> bookList);
    }
    public interface M{
        public void fetchBookInformation(String apiUrl);
    }
}
