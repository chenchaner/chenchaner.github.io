package com.example.dictionary.DailySaying;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IDailySaying;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class DailySayingModel extends BaseModel <DailySayingPresenter, IDailySaying.M>{
    String imageUrl;
    String TextSentence;
    String TextTranslation;
    String TextData;
    String ttsUrl;
    JSONObject jsonObject;
    public DailySayingModel(DailySayingPresenter mPresenter) {
        super(mPresenter);
    }

    private class FetchData extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            String apiUrl = strings[0];
            String TAG = "testForNull";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            Log.d(TAG, "fetchData: ");
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    return new JSONObject(responseData);
                }
            } catch (IOException | JSONException e) {
                Log.e("FetchDataTask", "Exception: ", e);
            }
            return null;
        }
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            if (jsonObject != null) {
                    imageUrl = jsonObject.optString("picture");
                    TextSentence = jsonObject.optString("content");
                    TextTranslation = jsonObject.optString("note");
                    TextData = jsonObject.optString("dateline");
                    ttsUrl = jsonObject.optString("tts");
                    mPresenter.getContract().setTtsUrl(ttsUrl);
                    mPresenter.getContract().setImageUrl(imageUrl);
                    mPresenter.getContract().setTextSentence(TextSentence);
                    mPresenter.getContract().setTextTranslation(TextTranslation);
                    mPresenter.getContract().setTextDate(TextData);
                } else {
                Log.e("FetchDataTask", "Response is null");
            }
        }
    }


    @Override
    public IDailySaying.M getContract() {
        return new IDailySaying.M() {
            @Override
            public void requestNetwork(String apiUrl) {
                String TAG = "fan";
                Log.d(TAG, "requestNetwork: "+apiUrl);
                new FetchData().execute(apiUrl);
            }
        };
    }
}
