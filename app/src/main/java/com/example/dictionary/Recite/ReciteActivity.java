package com.example.dictionary.Recite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dictionary.Base.BaseActivity;
import com.example.dictionary.Interface.IRecite;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.CarouselItemFragment;

import java.util.ArrayList;
import java.util.List;

public class ReciteActivity extends BaseActivity<RecitePresenter, IRecite.VP> {
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
        initView();
        initData();
        initListener();
        initClass();
        initAdapter();
    }

    @Override
    public IRecite.VP getContract() {
        return new IRecite.VP() {
            @Override
            public void initList(Context context) {
                mPresenter.getContract().initList(context);
            }

            @Override
            public void responseInitList(List<String> wordList, List<String> pronunciationList, List<String> translationList, List<String> ttsUrlList) {
                Log.d("WordList", "initList: "+wordList);
                runOnUiThread(()-> fragmentAdapter.setData(wordList, pronunciationList, translationList, ttsUrlList));
            }
        };
    }


    @Override
    public RecitePresenter getmPresenter() {
        return new RecitePresenter();
    }

    @Override
    public void initAdapter() {
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        getContract().initList(ReciteActivity.this);
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

    @Override
    public int getContentViewID() {
        return R.layout.activity_recite;
    }

    @Override
    public void initClass() {
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.recite_back);
        viewPager = findViewById(R.id.viewPage);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        back.setOnClickListener(this);
    }

    @Override
    public <ERROR> void responseError(Error error, Throwable throwable) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
    public class FragmentAdapter extends FragmentPagerAdapter {
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
            Log.d("setData", "setData: "+wordList);
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