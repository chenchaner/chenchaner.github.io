package com.example.dictionary.ReciteBook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.viewpager2.widget.ViewPager2;

import com.example.dictionary.Base.BaseActivity;
import com.example.dictionary.Interface.IReciteBook;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.CarouselAdapter;
import com.example.dictionary.RecyclerView.CarouselItemFragment;

public class ReciteBookActivity extends BaseActivity<ReciteBookPresenter, IReciteBook.VP> {
    private ViewPager2 viewPager;
    private ImageButton back;
    private String  id;
    private String courseNumber;

    private CarouselAdapter carouselAdapter;
    private int numberCou;
    private String apiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("id")) {
                id = intent.getStringExtra("id");
                courseNumber = intent.getStringExtra("courseNumber");
            }
        }
        numberCou = Integer.parseInt(courseNumber);
        initView();
        initData();
        initListener();
        initClass();
        initAdapter();
    }

    @Override
    public IReciteBook.VP getContract() {
        return new IReciteBook.VP() {
            @Override
            public void requestNetworkData(String apiUrl, int number) {
                mPresenter.getContract().requestNetworkData(apiUrl,number);
            }

            @Override
            public void responseForFetch(String word, String symbol, String desc, String soundUrl) {
                runOnUiThread(() -> {
                    CarouselItemFragment fragment = CarouselItemFragment.newInstance(word, symbol, desc, soundUrl);
                    carouselAdapter.addItem(fragment);
                });
            }
        };
    }

    @Override
    public ReciteBookPresenter getmPresenter() {
        return new ReciteBookPresenter();
    }

    @Override
    public void initAdapter() {
        carouselAdapter = new CarouselAdapter(this);
        viewPager.setAdapter(carouselAdapter);
        getContract().requestNetworkData(apiUrl,numberCou);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_recitebook;
    }

    @Override
    public void initClass() {
    }

    @Override
    public void initView() {
        back = findViewById(R.id.recite_back);
        viewPager = findViewById(R.id.viewPage);
    }

    @Override
    public void initData() {
        apiUrl = "http://rw.ylapi.cn/reciteword/wordlist.u?uid=12632&appkey=9ef142173278bfdf5403e4661ba2b6c6&class_id=" + id;
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
}