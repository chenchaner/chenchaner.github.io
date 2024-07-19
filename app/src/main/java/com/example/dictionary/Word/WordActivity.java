package com.example.dictionary.Word;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.dictionary.AuxiliaryClass.PlayMedia;
import com.example.dictionary.Base.BaseActivity;
import com.example.dictionary.Interface.IWord;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.Translation;
import com.example.dictionary.RecyclerView.TranslationAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordActivity extends BaseActivity<WordPresenter, IWord.VP> {
    private List<Translation> translations;
    private List<Translation> exchanges;
    private PlayMedia playMedia;
    private String EnMp3;
    private String AmMp3;
    private Drawable BeforeIcon;
    private Drawable AfterIcon;
    private String searchQuery;
    private ImageButton play_en;
    private ImageButton play_am;
    private ImageButton back;
    private ImageButton like;
    private TextView word;
    private TextView enSym;
    private TextView amSym;
    private TextView enText;
    private TextView amText;
    private TextView exchange;
    private String ttsUrl;
    private String tranForData;
    private String apiUrl;
    private RecyclerView recyclerView_translation;
    private LinearLayoutManager layoutManager_translation;
    private TranslationAdapter adapter;
    private RecyclerView recyclerView_exchange;
    private LinearLayoutManager layoutManager_exchange;
    private TranslationAdapter ExchangeAdapter;
    private String enT;



    @Override
    public WordPresenter getmPresenter() {
        return new WordPresenter();
    }

    @Override
    public void initAdapter() {
        layoutManager_translation = new LinearLayoutManager(this);
        recyclerView_translation.setLayoutManager(layoutManager_translation);
        layoutManager_exchange= new LinearLayoutManager(this);
        recyclerView_exchange.setLayoutManager(layoutManager_exchange);

        adapter = new TranslationAdapter(translations);
        recyclerView_translation.setAdapter(adapter);
        ExchangeAdapter = new TranslationAdapter(exchanges);
        recyclerView_exchange.setAdapter(ExchangeAdapter);
    }

    public void sendRequest() {
        apiUrl = "http://dict-co.iciba.com/api/dictionary.php?w=" + searchQuery + "&type=json&key=1F9CA812CB18FFDFC95FC17E9C57A5E1";

        if (containsChinese(searchQuery)){
            like.setVisibility(View.INVISIBLE);
            exchange.setVisibility(View.INVISIBLE);
            recyclerView_exchange.setVisibility(View.INVISIBLE);
            play_am.setVisibility(View.INVISIBLE);
            amText.setVisibility(View.INVISIBLE);
            enText.setText("音标");
            getContract().searchChinese(apiUrl);
        } else {
            Log.d("apiUrl", "api: "+apiUrl);
            getContract().searchEnglish(apiUrl);
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_word;
    }

    @Override
    public void initClass() {
        playMedia = new PlayMedia();
    }

    @Override
    public void initView() {
        recyclerView_translation = findViewById(R.id.translation_display);
        recyclerView_exchange = findViewById(R.id.exchange_display);
        like = (ImageButton) findViewById(R.id.like);
        word = (TextView) findViewById(R.id.word_name);
        word.setText(searchQuery);
        play_en = (ImageButton) findViewById(R.id.play_en);
        play_am = (ImageButton) findViewById(R.id.play_am);
        back = (ImageButton) findViewById(R.id.search_back);
        enText = (TextView) findViewById(R.id.en);
        amText = (TextView) findViewById(R.id.am);
        exchange = findViewById(R.id.exchange);
        enSym = findViewById(R.id.en);
        amSym = findViewById(R.id.am);
        word.setText(searchQuery);
        back = findViewById(R.id.search_back);
    }

    @Override
    public void initData() {
        EnMp3 = "";
        AmMp3 = "";
        BeforeIcon = getResources().getDrawable(R.drawable.like_before);
        AfterIcon = getResources().getDrawable(R.drawable.like_after);
        translations = new ArrayList<>();
        exchanges = new ArrayList<>();
    }

    @Override
    public void initListener() {
        like.setOnClickListener(this);
        play_am.setOnClickListener(this);
        play_en.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void responseError(Error error, Throwable throwable) {

    }
    @Override
    public IWord.VP getContract() {
        return new IWord.VP() {

            @Override
            public void initLikeIcon(Context context,String searchQuery) {
                mPresenter.getContract().initLikeIcon(context,searchQuery);
            }

            @Override
            public void initLikeIconResponse(boolean isLike) {
                if (isLike == true){
                    like.setImageDrawable(AfterIcon);
                } else {
                    like.setImageDrawable(BeforeIcon);
                }
            }

            @Override
            public void searchEnglish(String apiUrl) {
                mPresenter.getContract().searchEnglish(apiUrl);
            }

            @Override
            public void responseForSearchEnglish(List<Translation> translationsData, List<Translation> exchangesData, String en, String am, String phEnMp3, String phAmMp3, String phTts, String part, String firstTranslation) {
                Log.d("back","V层tran"+tranForData);
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      ttsUrl = phTts;
                                      EnMp3 = phEnMp3;
                                      AmMp3 = phAmMp3;
                                      enT = en;
                                      enText.setText(en);
                                      amText.setText(am);
                                      translations = translationsData;
                                      exchanges = exchangesData;
                                      tranForData = firstTranslation;
                                      initAdapter();
                                  }
                              }
                );
            }

            @Override
            public void searchChinese(String apiUrl) {
                mPresenter.getContract().searchChinese(apiUrl);
            }

            @Override
            public void responseForSearchChinese(List<Translation> translationsData, String en, String phEnMp3, String phAmMp3, String Tts, String part, String translation) {

                runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ttsUrl = Tts;
                                    EnMp3 = phEnMp3;
                                    AmMp3 = phAmMp3;
                                    enT = en;
                                    enText.setText(en);
                                    tranForData = translation;
                                    translations = translationsData;
                                    initAdapter();
                                }
                            });
            }

            @Override
            public void insertLike(String word, String translation, String pronunciation, String ttsUrl,Context context) {
                mPresenter.getContract().insertLike(word,translation,pronunciation,ttsUrl,context);
            }

            @Override
            public void deleteLike(String word,Context context) {
                mPresenter.getContract().deleteLike(word,context);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like:
                Drawable currentIcon = like.getDrawable();
                if (currentIcon.getConstantState().equals(BeforeIcon.getConstantState())) {
                    like.setImageDrawable(AfterIcon);
                    Toast.makeText(WordActivity.this, "已添加到生词本", Toast.LENGTH_SHORT).show();
                    getContract().insertLike(searchQuery,tranForData,enT,EnMp3,WordActivity.this);
                    Log.d("WordActivity", "EnMp3:"+EnMp3+" ttsUrl:"+ttsUrl);
                } else if (currentIcon.getConstantState().equals(AfterIcon.getConstantState())) {
                    like.setImageDrawable(BeforeIcon);
                    Toast.makeText(WordActivity.this, "已移除生词本", Toast.LENGTH_SHORT).show();

                    getContract().deleteLike(searchQuery,WordActivity.this);

                } else {
                    like.setImageDrawable(BeforeIcon);
                }
                break;
            case R.id.play_en:
                playMedia.play(EnMp3);
                break;
            case R.id.play_am:
                playMedia.play(AmMp3);
                break;
            case R.id.search_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        Log.d("SearchQuery", "onCreate: "+searchQuery);
        initView();
        initData();
        initListener();
        initClass();
        sendRequest();
        getContract().initLikeIcon(this,searchQuery);
    }

        private boolean containsChinese(String str) {
            String regex = "[\\u4e00-\\u9fa5]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
}