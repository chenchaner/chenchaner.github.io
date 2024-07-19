package com.example.dictionary.Interface;

import android.content.Context;

import java.util.List;

public interface IRecite {
    public interface VP{
        public void initList(Context context);
        public void responseInitList(List<String> wordList,List<String> pronunciationList,List<String> translationList,List<String> ttsUrlList);
    }
    public interface M{
        public void initList(Context context);
    }
}
