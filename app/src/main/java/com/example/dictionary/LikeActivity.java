package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity {
    private SearchView searchView;
    private ImageButton delete;
    private List<Like> originalLikeList = new ArrayList<>();
    private List<Like> likeList = new ArrayList<>();
    private List<String> letterIndexList = new ArrayList<>();
    private List<Like> filteredLikeList = new ArrayList<>();
    private SQLiteDatabase db_like;
    private ImageButton list;
    private LikeAdapter likeAdapter;
    RecyclerView recyclerView_letter;
    private LetterIndexAdapter letterIndexAdapter;


    protected void onResume() {
        super.onResume();
        BottomLayout bottomLayout = findViewById(R.id.bottom);
        bottomLayout.updateButtonStates("notebook");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(this);
        db_like = likeDatabaseManager.getDatabase();
        searchView = findViewById(R.id.search_dic);

        RecyclerView recyclerView_like= findViewById(R.id.like_display);
        LinearLayoutManager layoutManager_like= new LinearLayoutManager(this);
        recyclerView_like.setLayoutManager(layoutManager_like);
        likeList = getLikeItems();
        likeAdapter = new LikeAdapter(this,likeList);
        recyclerView_like.setAdapter(likeAdapter);
        originalLikeList = getLikeItems();

        recyclerView_letter = findViewById(R.id.letter_sort);
        LinearLayoutManager layoutManager_letter = new LinearLayoutManager(this);
        recyclerView_letter.setLayoutManager(layoutManager_letter);
        letterIndexList = getLetterItems();
        letterIndexAdapter = new LetterIndexAdapter(letterIndexList,likeList);
        recyclerView_letter.setAdapter(letterIndexAdapter);
        letterIndexAdapter.setLikeRecyclerView(recyclerView_like);

        delete = (ImageButton) findViewById(R.id.like_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LikeActivity.this);
                builder.setTitle("确定删除选中的历史记录？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Like> selectedLikes = new ArrayList<>();
                        for (int i = 0; i < likeAdapter.getItemCount(); i++) {
                            Like like = likeAdapter.getItemAtPosition(i);
                            if (like != null && like.isChecked()) {
                                selectedLikes.add(like);
                            }
                        }
                        for (Like like : selectedLikes) {
                            if (db_like != null) {
                                likeDatabaseManager.delete(db_like, like);
                            }
                        }
                        likeList.removeAll(selectedLikes);
                        likeAdapter.hideCheckBox();
                        likeAdapter.notifyDataSetChanged();
                        delete.setVisibility(View.INVISIBLE);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delete.setVisibility(View.INVISIBLE);
                        likeAdapter.hideCheckBox();
                    }
                });
                builder.show();
            }
        });



        list = (ImageButton) findViewById(R.id.sort);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredLikeList.clear();
                for (Like like: likeList){
                    if (like.getWord().contains(newText)){
                        filteredLikeList.add(like);
                    }
                }
                likeAdapter.updateData(filteredLikeList);
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                likeAdapter.updateData(likeList);
                return false;
            }
        });

    }
    private List<Like> getLikeItems() {
        List<Like> likes = new ArrayList<>();
        if (db_like != null) {
            Cursor cursor = db_like.query("LikeList", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex("word"));
                    @SuppressLint("Range") String translation = cursor.getString(cursor.getColumnIndex("translation"));
                    @SuppressLint("Range") String ttsUrl = cursor.getString(cursor.getColumnIndex("ttsUrl"));
                    Like word_l = new Like(word,translation,ttsUrl);
                    Log.d("LikeListdebug", "word:"+word+" translation:"+translation+" ttsUrl:"+ttsUrl);
                    likes.add(word_l);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } else {
            Log.e("LikeActivity", "Database connection is null");
        }
        return likes;
    }
    private List<String> getLetterItems(){
        List<String> letterIndices = new ArrayList<>();
        letterIndices = likeAdapter.getmLetterIndexList();
        return letterIndices;
    }

    private void showPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(this,list, Gravity.RIGHT);
        popupMenu.getMenuInflater().inflate(R.menu.list, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_item_default_sort:
                        likeList.clear();
                        likeList.addAll(originalLikeList);
                        likeAdapter.notifyDataSetChanged();
                        recyclerView_letter.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.menu_item_letter_sort:
                        likeAdapter.sort();
                        recyclerView_letter.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.menu_item_delete:
                        delete.setVisibility(View.VISIBLE);
                        likeAdapter.showCheckBox();
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

}