package com.example.dictionary.Interface;

import android.content.Context;

public interface IBookSearch {
    public interface VP{
        public void fetchBookNumber(Context context);
        public void responseBookNumber(String number);
    }
    public interface M{
        public void fetchBookNumber(Context context);
    }
}
