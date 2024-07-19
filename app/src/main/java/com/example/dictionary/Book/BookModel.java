package com.example.dictionary.Book;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.IBook;
import com.example.dictionary.RecyclerView.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookModel extends BaseModel<BookPresenter, IBook.M> {
    private List<Book> bookList = new ArrayList<>();
    public BookModel(BookPresenter mPresenter){
        super(mPresenter);
    }
    @Override
    public IBook.M getContract() {
        return new IBook.M() {
            @Override
            public void fetchBookInformation(String apiUrl) {
                new InitBooks().execute(apiUrl);
                Log.d("bookList", "fetchBookInformation: "+bookList);
            }
        };
    }
    public class InitBooks extends AsyncTask<String,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            String apiUrl = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
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

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                JSONArray datas = jsonObject.getJSONArray("datas");
                for (int i = 0; i < datas.length(); i++) {
                    JSONObject data = datas.getJSONObject(i);
                    String topTitle = data.has("title") ? data.getString("title") : null;
                    JSONArray childList = data.getJSONArray("child_list");
                    for (int j = 0; j < childList.length(); j++) {
                        JSONObject child = childList.getJSONObject(j);
                        String bookId = child.getString("class_id");
                        String title = child.getString("title");
                        String number = child.getString("word_num");
                        String courseNumber = child.getString("course_num");
                        if (topTitle != null) {
                            title = topTitle + ":" +title;
                        }
                        bookList.add(new Book(title, number, bookId,courseNumber));
                        Log.d("BookActivity:", " bookTitle:"+title+" bookId:"+bookId+" bookNumber:"+number);
                        Log.d("BookActivity","bookList"+bookList);
                    }
                    Log.d("bookList", "fetchBookInformation: "+bookList);
                    mPresenter.getContract().responseFetch(bookList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
