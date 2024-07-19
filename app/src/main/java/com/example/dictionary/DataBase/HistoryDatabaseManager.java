package com.example.dictionary.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDatabaseManager {
    private static HistoryDatabaseManager instance;
    private SQLiteDatabase database;

    private HistoryDatabaseManager(Context context) {
        database = DatabaseManager.getInstance(context).getDatabase();
    }

    public static synchronized HistoryDatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryDatabaseManager(context.getApplicationContext());
        }
        return instance;
    }
    public SQLiteDatabase getDatabase() {
        return database;
    }

}
