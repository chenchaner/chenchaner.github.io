package com.example.dictionary.Interface;

import android.content.Context;

import com.example.dictionary.RecyclerView.Translation;

import java.util.List;

public interface IWord {
    public interface VP{
        public void initLikeIcon(Context context,String searchQuery);
        public void initLikeIconResponse(boolean like);
        public void searchEnglish(String apiUrl);
        public void responseForSearchEnglish(List<Translation> translations, List<Translation> exchanges,String en,String am,String phEnMp3,String phAmMp3,String phTts,String part,String firstTranslation);
        public void searchChinese(String apiUrl);
        public void responseForSearchChinese(List<Translation> translations,String en,String phEnMp3,String phAmMp3,String phTts,String part,String translation);
        public void insertLike(String word,String translation,String pronunciation,String ttsUrl,Context context);
        public void deleteLike(String word,Context context);
    }
    public interface M{
        public void initLikeIcon(Context context,String searchQuery);
        public void searchEnglish(String apiUrl);
        public void searchChinese(String apiUrl);
        public void insertLike(String word,String translation,String pronunciation,String ttsUrl,Context context);
        public void deleteLike(String word,Context context);
    }
}
