package com.example.dictionary.DataBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dictionary.RecyclerView.Like;

import java.util.ArrayList;
import java.util.List;

public class LikeDatabaseManager {
    private static LikeDatabaseManager instance;
    private SQLiteDatabase database;

    private LikeDatabaseManager(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        if (databaseManager != null) {
            SQLiteDatabase db = databaseManager.getDatabase();
            Log.d("LikeDatabaseManager", "Success");
            if (db != null) {
                database = db;
                Log.d("LikeDatabaseManager", "Success to get database from DatabaseManager");
            } else {
                Log.e("LikeDatabaseManager", "Failed to get database from DatabaseManager");
            }
        } else {
            Log.e("LikeDatabaseManager", "DatabaseManager instance is null");
        }
    }

    public static synchronized LikeDatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new LikeDatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public void delete(SQLiteDatabase db, Like like){
        if(db != null && like !=null){
            int deletedRows = db.delete("LikeList", "word = ?", new String[]{like.getWord()});

            Log.d("LikeDatabaseManager", "Deleted " + deletedRows + " rows for word: " + like.getWord());
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public List<String> getWordList() {
        List<String> wordList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT word FROM LikeList", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex("word"));
                wordList.add(word);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return wordList;
    }

    public List<String> getPronunciationList() {
        List<String> pronunciationList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT pronunciation FROM LikeList", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String pronunciation = cursor.getString(cursor.getColumnIndex("pronunciation"));
                pronunciationList.add(pronunciation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return pronunciationList;
    }

    public List<String> getTranslationList() {
        List<String> translationList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT translation FROM LikeList", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String translation = cursor.getString(cursor.getColumnIndex("translation"));
                String translationText = translation;
                translationList.add(translationText);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return translationList;
    }

    public List<String> getTtsUrlList() {
        List<String> ttsUrlList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT ttsUrl FROM LikeList", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String ttsUrl = cursor.getString(cursor.getColumnIndex("ttsUrl"));
                ttsUrlList.add(ttsUrl);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return ttsUrlList;
    }

}
