package com.example.dictionary.Like;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.ILike;
import com.example.dictionary.DataBase.LikeDatabaseManager;
import com.example.dictionary.RecyclerView.Like;
import com.example.dictionary.RecyclerView.LikeAdapter;

import java.util.ArrayList;
import java.util.List;

public class LikeModel extends BaseModel<LikePresenter, ILike.M> {
    private SQLiteDatabase db_like;
    private List<Like> getLikeList = new ArrayList<>();
    private List<String> leIndexList = new ArrayList<>();
    private List<Like> updateList = new ArrayList<>();
    public LikeModel(LikePresenter mPresenter){
        super(mPresenter);
    }
    @Override
    public ILike.M getContract() {
        return new ILike.M() {
            @Override
            public void getLikeList(Context context) {
                getLikeList = getLikeItems(context);
                mPresenter.getContract().responseForLike(getLikeList);
            }

            @Override
            public void deleteAll(Context context, Like like) {
                deleteList(context,like);
            }

            @Override
            public void getLetter(LikeAdapter likeAdapter) {
                leIndexList = getLetterItems(likeAdapter);
                mPresenter.getContract().responseForLetter(leIndexList);
            }

            @Override
            public void updateList(Context context) {
                updateList = getLikeItems(context);
                mPresenter.getContract().responseForUpdate(updateList);
            }
        };
    }
    private List<Like> getLikeItems(Context context) {
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
        db_like = likeDatabaseManager.getDatabase();
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
    private void deleteList(Context context,Like like){
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
        likeDatabaseManager.delete(db_like, like);
    }
    private List<String> getLetterItems(LikeAdapter likeAdapter){
        List<String> letterIndices = new ArrayList<>();
        letterIndices = likeAdapter.getmLetterIndexList();
        return letterIndices;
    }
}
