package com.example.dictionary.Book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.Base.BaseActivity;
import com.example.dictionary.Interface.IBook;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.Book;
import com.example.dictionary.RecyclerView.BookAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends BaseActivity<BookPresenter, IBook.VP> {
    private String searchQuery;
    private TextView book;
    private ImageButton back;
    private RecyclerView recyclerView_book;
    private List<Book> bookRespensList = new ArrayList<>();
    private String apiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("search_query")) {
                searchQuery = intent.getStringExtra("search_query");
            }
        }
        initView();
        initData();
        initListener();
        initClass();
        getContract().fetchBookInformation(apiUrl);
    }

    @Override
    public IBook.VP getContract() {
        return new IBook.VP() {
            @Override
            public void fetchBookInformation(String apiUrl) {
                mPresenter.getContract().fetchBookInformation(apiUrl);
            }

            @Override
            public void responseFetch(List<Book> bookList) {
                runOnUiThread(()->{bookRespensList = bookList;
                    initAdapter();
                });
            }
        };
    }

    @Override
    public BookPresenter getmPresenter() {
        return new BookPresenter();
    }

    @Override
    public void initAdapter() {
        book.setText(searchQuery);
        Log.d("bookResponseList", "initAdapter: "+bookRespensList);
        recyclerView_book = (RecyclerView) findViewById(R.id.book_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_book.setLayoutManager(layoutManager);
        BookAdapter bookAdapter = new BookAdapter(this,bookRespensList);
        recyclerView_book.setAdapter(bookAdapter);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_book;
    }

    @Override
    public void initClass() {

    }

    @Override
    public void initView() {
        book = findViewById(R.id.book_name);
        back = (ImageButton) findViewById(R.id.book_back);
    }

    @Override
    public void initData() {
        apiUrl = "http://rw.ylapi.cn/reciteword/list.u?uid=12632&appkey=9ef142173278bfdf5403e4661ba2b6c6&name=" + searchQuery;
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