package com.example.dictionary.BookSearch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IBookSearch;
import com.example.dictionary.DataBase.LikeDatabaseManager;

public class BookSearchModel extends BaseModel<BookSearchPresenter, IBookSearch.M> {
    String wordNumber;
    private SQLiteDatabase db_like;
    public BookSearchModel(BookSearchPresenter mPresenter){
        super(mPresenter);
    }

    @Override
    public IBookSearch.M getContract() {
        return new IBookSearch.M() {
            @Override
            public void fetchBookNumber(Context context) {
                checkWordNumber(context);
                mPresenter.getContract().responseBookNumber(wordNumber);
            }
        };
    }
    public void checkWordNumber(Context context){
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
        db_like = likeDatabaseManager.getDatabase();
        Cursor cursor = db_like.rawQuery("SELECT COUNT(*) FROM LikeList",null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        wordNumber = String.valueOf(count);
    }
}
