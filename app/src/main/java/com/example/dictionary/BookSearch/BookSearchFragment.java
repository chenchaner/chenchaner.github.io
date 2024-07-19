package com.example.dictionary.BookSearch;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dictionary.Base.BaseFragment;
import com.example.dictionary.Book.BookActivity;
import com.example.dictionary.Interface.IBookSearch;
import com.example.dictionary.R;
import com.example.dictionary.Recite.ReciteActivity;

public class BookSearchFragment extends BaseFragment<BookSearchPresenter, IBookSearch.VP> {
    private ImageButton study;
    private SearchView searchView;
    private TextView number;
    @Override
    public void onResume() {
        super.onResume();
        getContract().fetchBookNumber(getActivity());
    }


    @Override
    public IBookSearch.VP getContract() {
        return new IBookSearch.VP() {
            @Override
            public void fetchBookNumber(Context context) {
                mPresenter.getContract().fetchBookNumber(getActivity());
            }

            @Override
            public void responseBookNumber(String bookNumber) {
                number.setText(bookNumber);
            }
        };
    }

    @Override
    public BookSearchPresenter getmPresenter() {
        return new BookSearchPresenter();
    }

    @Override
    public void initAdapter() {

    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_study;
    }

    @Override
    public void initClass() {

    }

    @Override
    public void initView(View v) {
        number = v.findViewById(R.id.word_number);
        study = (ImageButton) v.findViewById(R.id.study);
        searchView = (SearchView) v.findViewById(R.id.search_book);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getContext(), BookActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        getContract().fetchBookNumber(getActivity());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        study.setOnClickListener(this);
    }

    @Override
    public <ERROR> void responseError(Error error, Throwable throwable) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ReciteActivity.class);
        startActivity(intent);
    }
}