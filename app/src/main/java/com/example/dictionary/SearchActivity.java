package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private ImageButton back;
    private List<History> historyList = new ArrayList<>();
    private DictionaryDatabaseHelper historyHelper;
    private SQLiteDatabase db_history;
    private ImageButton delete;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        back = (ImageButton) findViewById(R.id.search_back);
        HistoryDatabaseManager historyDatabaseManager = HistoryDatabaseManager.getInstance(this);
        db_history = historyDatabaseManager.getDatabase();
        delete = (ImageButton) findViewById(R.id.delete);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,DailySayingActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView_history= findViewById(R.id.display_history);
//        LinearLayoutManager layoutManager_history= new LinearLayoutManager(this);
        StaggeredGridLayoutManager layoutManager_history = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        recyclerView_history.setLayoutManager(layoutManager_history);
        historyList = getHistoryItems();
        HistoryAdapter historyAdapter = new HistoryAdapter(this,historyList);
        recyclerView_history.setAdapter(historyAdapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("确定删除全部历史记录？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_history.delete("History",null,null);
                        List<History> updatedList = new ArrayList<>();
                        historyAdapter.updateList(updatedList);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                historyAdapter.notifyDataSetChanged();
            }
        });
        searchView = findViewById(R.id.search_v);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(SearchActivity.this, WordActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);

                SQLiteDatabase db_history = DatabaseManager.getInstance(SearchActivity.this).getDatabase();

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
                historyAdapter.updateList(updatedList);

                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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