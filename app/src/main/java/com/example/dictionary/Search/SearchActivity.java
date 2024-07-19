package com.example.dictionary.Search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.dictionary.RecyclerView.History;
import com.example.dictionary.RecyclerView.HistoryAdapter;
import com.example.dictionary.Base.BaseActivity;
import com.example.dictionary.Interface.ISearch;
import com.example.dictionary.DataBase.HistoryDatabaseManager;
import com.example.dictionary.DataBase.DictionaryDatabaseHelper;
import com.example.dictionary.R;
import com.example.dictionary.Word.WordActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter, ISearch.VP> {
    private ImageButton back;
    private RecyclerView recyclerView_history;
    public List<History> hisList = new ArrayList<>();
    public List<History> dataList = new ArrayList<>();
    private DictionaryDatabaseHelper historyHelper;
    private HistoryAdapter historyAdapter;
    private SQLiteDatabase db_history;
    private ImageButton delete;
    private SearchView searchView;

    @Override
    public ISearch.VP getContract() {
        return new ISearch.VP() {
            @Override
            public void deleteAllHistoryRequest() {
                mPresenter.getContract().deleteAllHistoryRequest();
            }

            @Override
            public void deleteAllResponse(List<History> historyList) {
                dataList = historyList;
                historyAdapter.updateList(dataList);
            }

            @Override
            public void searchRequest(String query, Context context) {
                mPresenter.getContract().searchRequest(query,SearchActivity.this);
            }

            @Override
            public void searchResponse(List<History> historyList) {
                hisList = historyList;
            }

            @Override
            public void initList(Context context) {
                mPresenter.getContract().initList(SearchActivity.this);
            }

            @Override
            public void initListResponse(List<History> historyList) {
                hisList = historyList;
            }
        };
    }

    @Override
    public SearchPresenter getmPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        initView();
        initData();
        initAdapter();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                getContract().searchRequest(query,SearchActivity.this);

                Intent intent = new Intent(SearchActivity.this, WordActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);


                historyAdapter.updateList(dataList);
                Log.d("SearchActivity", "onQueryTextSubmit: "+dataList);

                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void initView() {
        back = findViewById(R.id.search_back);
        delete = findViewById(R.id.delete);
        searchView = findViewById(R.id.search_v);
        recyclerView_history = findViewById(R.id.display_history);
    }

    @Override
    public void initAdapter() {
        StaggeredGridLayoutManager layoutManager_history = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_history.setLayoutManager(layoutManager_history);
        getContract().initList(SearchActivity.this);
        historyAdapter = new HistoryAdapter(this, hisList);
        recyclerView_history.setAdapter(historyAdapter);
    }

    public void sendRequest() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_search;
    }

    @Override
    public void initClass() {
    }


    @Override
    public void initData() {
        HistoryDatabaseManager historyDatabaseManager = HistoryDatabaseManager.getInstance(this);
        db_history = historyDatabaseManager.getDatabase();
    }

    @Override
    public void initListener() {
        back.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public <ERROR> void responseError(Error error, Throwable throwable) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete:
                AlertDialog.Builder builder = getBuilder();
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                historyAdapter.notifyDataSetChanged();
                break;
            case R.id.search_back:
                finish();
                break;
        }
    }

    @NonNull
    private AlertDialog.Builder getBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("确定删除全部历史记录？");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContract().deleteAllHistoryRequest();
            }
        });
        return builder;
    }
}