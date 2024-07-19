package com.example.dictionary.Search;

import android.content.Context;
import android.util.Log;

import com.example.dictionary.RecyclerView.History;
import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.ISearch;

import java.util.List;

public class SearchPresenter extends BasePresenter<SearchActivity,SearchModel, ISearch.VP> {
    @Override
    public SearchModel getmModelInstance() {
        return new SearchModel(this);
    }

    @Override
    public ISearch.VP getContract() {
        return new ISearch.VP() {
            @Override
            public void deleteAllHistoryRequest() {
                mModel.getContract().deleteAllHistoryRequest();
            }

            @Override
            public void deleteAllResponse(List<History> historyList) {
                mView.getContract().deleteAllResponse(historyList);
            }

            @Override
            public void searchRequest(String query, Context context) {
                mModel.getContract().searchRequest(query,context);
                Log.d("searchRequest", "searchRequest: ");
            }

            @Override
            public void searchResponse(List<History> historyList) {
                mView.getContract().searchResponse(historyList);
            }

            @Override
            public void initList(Context context) {
                mModel.getContract().initList(context);
            }

            @Override
            public void initListResponse(List<History> historyList) {
                mView.getContract().initListResponse(historyList);
            }

        };
    }
}
