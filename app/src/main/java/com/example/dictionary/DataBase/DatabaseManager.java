package com.example.dictionary.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    private static DatabaseManager instance;
    private SQLiteDatabase database;

    private DatabaseManager(Context context) {

        DictionaryDatabaseHelper dbHelper = new DictionaryDatabaseHelper(context, "Dictionary.db", null, 1);
        if (dbHelper != null) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
