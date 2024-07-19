package com.example.dictionary.Interface;

import android.content.Context;

import com.example.dictionary.RecyclerView.History;

import java.util.List;

public interface ISearch {
    public interface VP{
        public void deleteAllHistoryRequest();
        public void deleteAllResponse(List<History> historyList);
        public void searchRequest(String query, Context context);
        public void searchResponse(List<History> historyList);
        public void initList(Context context);
        public void initListResponse(List<History> historyList);

    }
    public interface M{
        public void deleteAllHistoryRequest();
        public void searchRequest(String query,Context context);
        public void initList(Context context);
    }
}
