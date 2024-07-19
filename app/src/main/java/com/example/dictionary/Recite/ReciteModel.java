package com.example.dictionary.Recite;

import android.content.Context;
import android.util.Log;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IRecite;
import com.example.dictionary.DataBase.LikeDatabaseManager;

import java.util.List;

public class ReciteModel extends BaseModel<RecitePresenter,IRecite.M> {
    private List<String> wordList;
    private List<String> pronunciationList;
    private List<String> translationList;
    private List<String> ttsUrlList;
    public ReciteModel(RecitePresenter mPresenter){
        super(mPresenter);
    }
    @Override
    public IRecite.M getContract() {
        return new IRecite.M() {
            @Override
            public void initList(Context context) {
                fetchList(context);
                Log.d("wordList", "initList: "+wordList);
                mPresenter.getContract().responseInitList(wordList,pronunciationList,translationList,ttsUrlList);
            }
        };
    }
    public void fetchList(Context context){
        LikeDatabaseManager like_db = LikeDatabaseManager.getInstance(context);
        wordList = like_db.getWordList();
        pronunciationList = like_db.getPronunciationList();
        translationList = like_db.getTranslationList();
        ttsUrlList = like_db.getTtsUrlList();
    }
}
