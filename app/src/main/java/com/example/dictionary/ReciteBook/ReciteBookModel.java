package com.example.dictionary.ReciteBook;

import androidx.annotation.NonNull;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IReciteBook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReciteBookModel extends BaseModel<ReciteBookPresenter, IReciteBook.M> {
    public ReciteBookModel(ReciteBookPresenter mPresenter){
        super(mPresenter);
    }
    @Override
    public IReciteBook.M getContract() {
        return new IReciteBook.M() {
            @Override
            public void requestNetworkData(String apiUrl, int number) {
                fetchBookInformation(apiUrl,number);
            }
        };
    }
    public void fetchBookInformation(String apiUrl, int numberCou){
        OkHttpClient client = new OkHttpClient();
        for (int i = 1;i <= numberCou; i++) {
            String url = apiUrl+ "&course=" + i;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String jsonData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONArray datas = jsonObject.getJSONArray("datas");
                            for(int j = 0;j<datas.length();j++){
                                JSONObject data = datas.getJSONObject(j);
                                String word = data.getString("name");
                                String symbol = data.optString("symbol", "N/A");
                                String desc = data.getString("desc");
                                String soundUrl = data.optString("sound", "");
                                mPresenter.getContract().responseForFetch(word, symbol, desc, soundUrl);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {

                    }
                }
            });
        }
    }
}
