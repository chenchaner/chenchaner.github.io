package com.example.dictionary.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IWord;
import com.example.dictionary.DataBase.DictionaryDatabaseHelper;
import com.example.dictionary.DataBase.LikeDatabaseManager;
import com.example.dictionary.RecyclerView.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordModel extends BaseModel<WordPresenter, IWord.M> {
    boolean like;
    private DictionaryDatabaseHelper historyHelper;
    private SQLiteDatabase db_history;
    private DictionaryDatabaseHelper likeHelper;
    private SQLiteDatabase db_like;
    private String en;
    private String am;
    private String phEnMp3;
    private String phAmMp3;
    private String phTts;
    private String translation;
    private String firstTranslation;
    private String part;
    private List<Translation> translations = new ArrayList<>();
    private List<Translation> exchanges = new ArrayList<>();

    public WordModel(WordPresenter mPresenter) {
        super(mPresenter);
    }

    @Override
    public IWord.M getContract() {
        return new IWord.M() {
            @Override
            public void initLikeIcon(Context context, String searchQuery) {
                like = judgeLike(context, searchQuery);
                mPresenter.getContract().initLikeIconResponse(like);
            }

            @Override
            public void searchEnglish(String apiUrl) {
                new FetchEnglish().execute(apiUrl);
            }

            @Override
            public void searchChinese(String apiUrl) {
                new FetchChinese().execute(apiUrl);
                mPresenter.getContract().responseForSearchChinese(translations,en,phEnMp3,phAmMp3,phTts,part,translation);
            }

            @Override
            public void insertLike(String word, String translation, String pronunciation, String ttsUrl,Context context) {
                ContentValues values = new ContentValues();
                values.put("word",word);
                values.put("translation",translation);
                values.put("pronunciation",pronunciation);
                values.put("ttsUrl",ttsUrl);
                LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
                db_like = likeDatabaseManager.getDatabase();
                db_like.insert("LikeList",null,values);

            }

            @Override
            public void deleteLike(String word,Context context) {
                LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
                db_like = likeDatabaseManager.getDatabase();
                db_like.delete("LikeList", "word=?", new String[]{word});

            }
        };
    }

    public boolean judgeLike(Context context, String searchQuery) {
        boolean likeResult;
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
        db_like = likeDatabaseManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put("word", searchQuery);

        Cursor cursor = db_like.rawQuery("SELECT * FROM LikeList WHERE word = ?", new String[]{searchQuery});
        if (cursor != null && cursor.moveToFirst()) {
            likeResult = true;
        } else {
            likeResult = false;
        }
        return likeResult;
    }


    public class FetchChinese extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            String TAG = "WordModel";
            String apiUrl = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            Log.d(TAG, "fetchData: ");
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null){

                try {
                    JSONArray symbolsArray = jsonObject.getJSONArray("symbols");
                    JSONObject firstSymbol = symbolsArray.getJSONObject(0);
                    en = firstSymbol.optString("word_symbol");
                    phEnMp3 = firstSymbol.optString("symbol_mp3");
                    JSONArray partsArray = firstSymbol.getJSONArray("parts");
                    for (int i = 0; i < partsArray.length(); i++) {
                        JSONObject partObject = partsArray.getJSONObject(i);
                        part = partObject.optString("part_name");
                        JSONArray meansArray = partObject.getJSONArray("means");
                        StringBuilder translationStringBuilder = new StringBuilder();
                        for (int j = 0; j < meansArray.length(); j++) {
                            JSONObject meanObject = meansArray.getJSONObject(j);
                            String mean = meanObject.optString("word_mean");
                            translationStringBuilder.append(mean);
                            if (j < meansArray.length() - 1) {
                                translationStringBuilder.append("; ");
                            }
                        }
                        translation = translationStringBuilder.toString();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public class FetchEnglish extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            String TAG = "WordModel";
            String apiUrl = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            Log.d(TAG, "fetchData: ");
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null){

                try {
                    JSONArray symbolsArray = jsonObject.getJSONArray("symbols");
                    JSONObject firstSymbol = symbolsArray.getJSONObject(0);

                    en = firstSymbol.optString("ph_en");
                    am = firstSymbol.optString("ph_am");
                    Log.d("am", "onResponse: "+am);
                    JSONArray partsArray = firstSymbol.getJSONArray("parts");
                    for (int i = 0; i < partsArray.length(); i++) {
                        JSONObject partObject = partsArray.getJSONObject(i);
                        String part = partObject.optString("part");
                        JSONArray meansArray = partObject.getJSONArray("means");
                        StringBuilder translationStringBuilder = new StringBuilder();
                        for (int j = 0; j < meansArray.length(); j++) {
                            String mean = meansArray.getString(j);
                            translationStringBuilder.append(mean);
                            if (j < meansArray.length() - 1) {
                                translationStringBuilder.append("; ");
                            }
                        }
                        translation = translationStringBuilder.toString();
                        if (i == 0){
                            firstTranslation = translation;
                        }
                        Log.d("Translation", "translation"+translation);
                        translations.add(new Translation(part, translation));
                        phEnMp3 = firstSymbol.optString("ph_en_mp3");
                        phAmMp3 = firstSymbol.optString("ph_am_mp3");
                        phTts = firstSymbol.optString("ph_tts_mp3");
                        Log.d("phTts", "onResponse: "+phTts);
                    }

                    JSONObject exchangeObject = jsonObject.optJSONObject("exchange");
                    if (exchangeObject != null) {
                        exchanges.clear();
                        Iterator<String> keys = exchangeObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String key_Chinese = changeToChinese(key);
                            String value = exchangeObject.optString(key);
                            if (!value.isEmpty()) {
                                exchanges.add(new Translation(key_Chinese, value));
                                Log.d("TranslationDebug", "key: " + key_Chinese + ", value: " + value);
                            }
                        }


                    }
                    Log.d("m:seE", "searchEnglish: "+translations+exchanges+en+am+phEnMp3+phAmMp3+phTts+part+firstTranslation);
                    mPresenter.getContract().responseForSearchEnglish(translations,exchanges,en,am,phEnMp3,phAmMp3,phTts,part,firstTranslation);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    private String changeToChinese(String key) {
        switch (key) {
            case "word_pl":
                return "复数:";
            case "word_third":
                return "第三人称单数:";
            case "word_past":
                return "过去式:";
            case "word_done":
                return "过去分词:";
            case "word_ing":
                return "现在式:";
            case "word_er":
                return "比较级:";
            case "word_est":
                return "最高级:";
            default:
                return "未知";
        }
    }
}


