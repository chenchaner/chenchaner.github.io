package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class BookSearchActivity extends AppCompatActivity {
    private ImageButton study;
    private SearchView searchView;
    private SQLiteDatabase db_like;
    private TextView number;
    protected void onResume() {
        super.onResume();
        BottomLayout bottomLayout = findViewById(R.id.bottom);
        bottomLayout.updateButtonStates("recite");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(this);
        db_like = likeDatabaseManager.getDatabase();
        Cursor cursor = db_like.rawQuery("SELECT COUNT(*) FROM LikeList",null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        String countString = String.valueOf(count);
        number = findViewById(R.id.word_number);
        number.setText(countString);

        study = (ImageButton) findViewById(R.id.study);
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookSearchActivity.this,ReciteActivity.class);
                startActivity(intent);
            }
        });
        searchView = (SearchView) findViewById(R.id.search_book);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(BookSearchActivity.this, BookActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}