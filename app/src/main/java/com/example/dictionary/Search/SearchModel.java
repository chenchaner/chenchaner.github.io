package com.example.dictionary.Search;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.RecyclerView.History;
import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.ISearch;
import com.example.dictionary.DataBase.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class SearchModel extends BaseModel<SearchPresenter, ISearch.M> {
    private RecyclerView recyclerView_history;
    private List<History> historyList = new ArrayList<>();
    private SQLiteDatabase db_history;
    public SearchModel (SearchPresenter mPresenter){
        super(mPresenter);
    }

    @Override
    public ISearch.M getContract() {
        return new ISearch.M() {

            @Override
            public void deleteAllHistoryRequest() {
                db_history.delete("History",null,null);
                List<History> updatedList = new ArrayList<>();
                mPresenter.getContract().deleteAllResponse(updatedList);
            }

            @Override
            public void searchRequest(String query, Context context) {
                SQLiteDatabase db_history = DatabaseManager.getInstance(context).getDatabase();
                long currentTimestamp = System.currentTimeMillis();
                ContentValues values = new ContentValues();
                values.put("english", query);
                values.put("timestamp", currentTimestamp);

                Cursor cursor = db_history.rawQuery("SELECT * FROM History WHERE english = ?", new String[]{query});

                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex("id"));
                    db_history.update("History", values, "id = ?", new String[]{String.valueOf(id)});
                } else {
                    db_history.insert("History", null, values);
                }

                if (cursor != null) {
                    cursor.close();
                }
                List<History> updatedList = getHistoryItems();
                mPresenter.getContract().searchResponse(updatedList);

            }

            @Override
            public void initList(Context context) {
                db_history = DatabaseManager.getInstance(context).getDatabase();
                historyList = getHistoryItems();
                mPresenter.getContract().searchResponse(historyList);
            }
        };
    }
    private List<History> getHistoryItems() {
        List<History> history = new ArrayList<>();
        Cursor cursor = db_history.query("History", null, null, null, null, null,  "timestamp DESC");
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String english = cursor.getString(cursor.getColumnIndex("english"));
                History his = new History(english);
                history.add(his);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return history;
    }
}
