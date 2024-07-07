package com.example.dictionary;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ReciteActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ImageView back;

    private FragmentAdapter fragmentAdapter;
    private List<String> wordList;
    private List<String> pronunciationList;
    private List<String> translationList;
    private List<String> ttsUrlList;
    private int currentPosition = 0;
    private SQLiteDatabase db_like;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recite);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        back = (ImageView) findViewById(R.id.recite_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReciteActivity.this,BookSearchActivity.class);
                startActivity(intent);
            }
        });
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPage);
        viewPager.setAdapter(fragmentAdapter);
        LikeDatabaseManager like_db = LikeDatabaseManager.getInstance(this);
        wordList = like_db.getWordList();
        pronunciationList = like_db.getPronunciationList();
        translationList = like_db.getTranslationList();
        ttsUrlList = like_db.getTtsUrlList();
        fragmentAdapter.setData(wordList, pronunciationList, translationList, ttsUrlList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<String> wordList;
        private List<String> pronunciationList;
        private List<String> translationList;
        private List<String> ttsUrlList;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            wordList = new ArrayList<>();
            pronunciationList = new ArrayList<>();
            translationList = new ArrayList<>();
            ttsUrlList = new ArrayList<>();
        }
        public void setData(List<String> wordList, List<String> pronunciationList, List<String> translationList, List<String> ttsUrlList) {
            this.wordList = wordList;
            this.pronunciationList = pronunciationList;
            this.translationList = translationList;
            this.ttsUrlList = ttsUrlList;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            String word = wordList.get(position);
            String pronunciation = pronunciationList.get(position);
            String translation = translationList.get(position);
            String ttsUrl = ttsUrlList.get(position);
            return CarouselItemFragment.newInstance(word, pronunciation, translation, ttsUrl);
        }

        @Override
        public int getCount() {
            return wordList.size();
        }
    }
}