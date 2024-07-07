package com.example.dictionary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordActivity extends AppCompatActivity {
    private List<Translation> translations = new ArrayList<>();
    private List<Translation> exchanges = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    String phEnMp3 = "";
    String phAmMp3 = "";
    private Drawable BeforeIcon;
    private Drawable AfterIcon;
    private String searchQuery;
    private ImageButton play_en;
    private ImageButton play_am;
    private ImageButton back;
    private ImageButton like;
    private TextView word;
    private TextView en;
    private TextView am;
    private String ttsUrl;
    private DictionaryDatabaseHelper historyHelper;
    private SQLiteDatabase db_history;
    private DictionaryDatabaseHelper likeHelper;
    private SQLiteDatabase db_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_word);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra("search_query")) {
                searchQuery = intent.getStringExtra("search_query");
            }
        }
        mediaPlayer = new MediaPlayer();

        like = (ImageButton) findViewById(R.id.like);
        BeforeIcon = getResources().getDrawable(R.drawable.like_before);
        AfterIcon = getResources().getDrawable(R.drawable.like_after);
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(this);
        db_like = likeDatabaseManager.getDatabase();
        ContentValues values = new ContentValues();
        values.put("word",searchQuery);

        Cursor cursor = db_like.rawQuery("SELECT * FROM LikeList WHERE word = ?", new String[]{searchQuery});
        if (cursor != null && cursor.moveToFirst()){
            like.setImageDrawable(AfterIcon);
        }else {
            like.setImageDrawable(BeforeIcon);
        }

        word = (TextView) findViewById(R.id.word_name);
        word.setText(searchQuery);
        play_en = (ImageButton) findViewById(R.id.play_en);
        play_am = (ImageButton) findViewById(R.id.play_am);
        back = (ImageButton) findViewById(R.id.search_back);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable currentIcon = like.getDrawable();
                if (currentIcon.getConstantState().equals(BeforeIcon.getConstantState())) {
                    like.setImageDrawable(AfterIcon);
                    Toast.makeText(WordActivity.this, "已添加到生词本", Toast.LENGTH_SHORT).show();
                    db_like.insert("LikeList",null,values);
                    Log.d("WordActivity","values:"+values);
                } else if (currentIcon.getConstantState().equals(AfterIcon.getConstantState())) {
                    like.setImageDrawable(BeforeIcon);
                    Toast.makeText(WordActivity.this, "已移除生词本", Toast.LENGTH_SHORT).show();
                    db_like.delete("LikeList", "word=?", new String[]{searchQuery});
                } else {
                    like.setImageDrawable(BeforeIcon);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordActivity.this, DailySayingActivity.class);
                startActivity(intent);
            }
        });
        play_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndPlayAudio(phEnMp3, "en");
                Log.d("phEnMp3", "phEnMp3: "+phEnMp3);
            }
        });
        play_am.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndPlayAudio(phAmMp3, "an");
                Log.d("phAnMp3", "phAnMp3: "+phAmMp3);
            }
        });



        en = (TextView) findViewById(R.id.en);
        am = (TextView) findViewById(R.id.am);

        RecyclerView recyclerView_translation = findViewById(R.id.translation_display);
        LinearLayoutManager layoutManager_translation = new LinearLayoutManager(this);
        recyclerView_translation.setLayoutManager(layoutManager_translation);
        TranslationAdapter adapter = new TranslationAdapter(translations);
        recyclerView_translation.setAdapter(adapter);

        RecyclerView recyclerView_exchange = findViewById(R.id.exchange_display);
        LinearLayoutManager layoutManager_exchange= new LinearLayoutManager(this);
        recyclerView_exchange.setLayoutManager(layoutManager_exchange);
        TranslationAdapter ExchangeAdapter = new TranslationAdapter(exchanges);
        recyclerView_exchange.setAdapter(ExchangeAdapter);

        String apiUrl = "http://dict-co.iciba.com/api/dictionary.php?w=" + searchQuery + "&type=json&key=1F9CA812CB18FFDFC95FC17E9C57A5E1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        if (containsChinese(searchQuery)) {
            like.setVisibility(View.INVISIBLE);
            TextView en = findViewById(R.id.english_en);
            TextView am = findViewById(R.id.english_am);
            TextView exchange = findViewById(R.id.exchange);
            exchange.setVisibility(View.INVISIBLE);
            recyclerView_exchange.setVisibility(View.INVISIBLE);
            play_am.setVisibility(View.INVISIBLE);
            am.setVisibility(View.INVISIBLE);
            en.setText("音标");
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String responseData = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray symbolsArray = jsonObject.getJSONArray("symbols");
                            JSONObject firstSymbol = symbolsArray.getJSONObject(0);
                            String wordSymbol = firstSymbol.optString("word_symbol");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    en.setText(wordSymbol);
                                }
                            });
                            phEnMp3 = firstSymbol.optString("symbol_mp3");
                            JSONArray partsArray = firstSymbol.getJSONArray("parts");
                            translations.clear();
                            for (int i = 0; i < partsArray.length(); i++) {
                                JSONObject partObject = partsArray.getJSONObject(i);
                                String part = partObject.optString("part_name");
                                JSONArray meansArray = partObject.getJSONArray("means");
                                StringBuilder translationStringBuilder = new StringBuilder();
                                for (int j = 0;j < meansArray.length();j++){
                                    JSONObject meanObject = meansArray.getJSONObject(j);
                                    String mean = meanObject.optString("word_mean");
                                    translationStringBuilder.append(mean);
                                    if (j < meansArray.length() - 1) {
                                        translationStringBuilder.append("; ");
                                    }
                                }
                                String translation = translationStringBuilder.toString();
                                translations.add(new Translation(part, translation));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });




        } else {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray symbolsArray = jsonObject.getJSONArray("symbols");

                            JSONObject firstSymbol = symbolsArray.getJSONObject(0);

                            String TextEn = firstSymbol.optString("ph_en");
                            String TextAm = firstSymbol.optString("ph_am");
                            values.put("pronunciation",TextAm);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    en.setText(TextEn);
                                    am.setText(TextAm);
                                }
                            });
                            JSONArray partsArray = firstSymbol.getJSONArray("parts");
                            translations.clear();


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
                                String translation = translationStringBuilder.toString();
                                translations.add(new Translation(part, translation));
                                values.put("translation",part+translation);
                                phEnMp3 = firstSymbol.optString("ph_en_mp3");
                                phAmMp3 = firstSymbol.optString("ph_am_mp3");
                                String phTts = firstSymbol.optString("ph_tts_mp3");
                                values.put("ttsUrl", phEnMp3);
                                Log.d("TranslationDebug", "Part: " + part + ", Translation: " + translation);

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ExchangeAdapter.notifyDataSetChanged();
                                }
                            });




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }
            });
        }



    }


    private String changeToChinese(String key){
        switch (key){
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



    private void fetchAndPlayAudio(String audioUrl, String accent) {
        playAudio(audioUrl);
    }

    private void playAudio(String audioUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

        private boolean containsChinese(String str) {
            String regex = "[\\u4e00-\\u9fa5]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
}




