package com.example.dictionary.Word;

import android.content.Context;
import android.util.Log;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.IWord;
import com.example.dictionary.RecyclerView.Translation;

import java.util.List;

public class WordPresenter extends BasePresenter<WordActivity,WordModel, IWord.VP> {
    @Override
    public WordModel getmModelInstance() {
        return new WordModel(this);
    }

    @Override
    public IWord.VP getContract() {
        return new IWord.VP() {
            @Override
            public void initLikeIcon(Context context,String searchQuery) {
                mModel.getContract().initLikeIcon(context,searchQuery);
            }

            @Override
            public void initLikeIconResponse(boolean like) {
                mView.getContract().initLikeIconResponse(like);
                Log.d("isLike", "isLike"+like);
            }

            @Override
            public void searchEnglish(String apiUrl) {
                mModel.getContract().searchEnglish(apiUrl);
            }

            @Override
            public void responseForSearchEnglish(List<Translation> translations, List<Translation> exchanges, String en, String am, String phEnMp3, String phAmMp3, String phTts, String part, String firstTranslation) {
                mView.getContract().responseForSearchEnglish(translations,exchanges,en,am,phEnMp3,phAmMp3,phTts,part,firstTranslation);
                Log.d("P层", "P"+translations);
            }

            @Override
            public void searchChinese(String apiUrl) {
                mModel.getContract().searchChinese(apiUrl);
            }

            @Override
            public void responseForSearchChinese(List<Translation> translations, String en, String phEnMp3, String phAmMp3, String phTts, String part, String translation) {
                mView.getContract().responseForSearchChinese(translations,en,phEnMp3,phAmMp3,phTts,part,translation);
            }

            @Override
            public void insertLike(String word, String translation, String pronunciation, String ttsUrl,Context context) {
                mModel.getContract().insertLike(word, translation, pronunciation, ttsUrl,context);
            }

            @Override
            public void deleteLike(String word,Context context) {
                mModel.getContract().deleteLike(word,context);
            }
        };
    }
}
