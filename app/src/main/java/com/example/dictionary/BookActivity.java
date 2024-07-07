package com.example.dictionary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookActivity extends AppCompatActivity {
    private String searchQuery;
    private TextView book;
    private ImageButton back;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra("search_query")) {
                searchQuery = intent.getStringExtra("search_query");
            }
        }
        book = findViewById(R.id.book_name);
        book.setText(searchQuery);
        back = (ImageButton) findViewById(R.id.book_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this,BookSearchActivity.class);
                startActivity(intent);
            }
        });
        initBooks();
        RecyclerView recyclerView_book = (RecyclerView) findViewById(R.id.book_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_book.setLayoutManager(layoutManager);
        BookAdapter bookAdapter = new BookAdapter(this,bookList);
        recyclerView_book.setAdapter(bookAdapter);
    }

    private void initBooks() {
        String apiUrl = "http://rw.ylapi.cn/reciteword/list.u?uid=12632&appkey=9ef142173278bfdf5403e4661ba2b6c6&name=" + searchQuery;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
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
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

}