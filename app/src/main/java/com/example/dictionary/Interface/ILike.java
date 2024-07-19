package com.example.dictionary.Interface;

import android.content.Context;

import com.example.dictionary.RecyclerView.Like;
import com.example.dictionary.RecyclerView.LikeAdapter;

import java.util.List;

public interface ILike {
    public interface VP{
        public void getLikeList(Context context);
        public void responseForLike(List<Like> getLikeList);
        public void deleteAll(Context context,Like like);
        public void responseForeDelete();
        public void getLetter(LikeAdapter likeAdapter);
        public void responseForLetter(List<String> letterIndexList);
        public void updateList(Context context);
        public void responseForUpdate(List<Like> updateList);
    }
    public interface M{
        public void getLikeList(Context context);
        public void deleteAll(Context context,Like like);
        public void getLetter(LikeAdapter likeAdapter);
        public void updateList(Context context);
    }
}
