package com.example.dictionary.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.Serializable;

public class DictionaryDatabaseHelper extends SQLiteOpenHelper implements Serializable {
    public static final String CREATE_HISTORY = "create table History ("
            +"id integer primary key autoincrement,"
            +"english text,"
            +"timestamp integer)";
    private static final String CREATE_LIKELIST = "create table LikeList ("
            +"id integer primary key autoincrement,"
            +"word text,"
            +"translation text,"
            +"pronunciation text,"
            + "ttsUrl text)";
    private Context mContext;
    public DictionaryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY);
        db.execSQL(CREATE_LIKELIST);
        Toast.makeText(mContext, "succeed-history", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}

