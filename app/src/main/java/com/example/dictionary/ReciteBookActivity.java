package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReciteBookActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private ImageView back;
    private String  id;
    private String courseNumber;

    private CarouselAdapter carouselAdapter;
    private int i = 1;
    private int numberCou;

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
        setContentView(R.layout.activity_recitebook);
        back = (ImageView) findViewById(R.id.recite_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReciteBookActivity.this,BookSearchActivity.class);
                startActivity(intent);
            }
        });
        numberCou = Integer.parseInt(courseNumber);
        viewPager = findViewById(R.id.viewPage);
        carouselAdapter = new CarouselAdapter(this);
        viewPager.setAdapter(carouselAdapter);
        OkHttpClient client = new OkHttpClient();
        for (; i <= numberCou; i++) {
            String apiUrl = "http://rw.ylapi.cn/reciteword/wordlist.u?uid=12632&appkey=9ef142173278bfdf5403e4661ba2b6c6&class_id=" + id + "&course=" + i;
            Request request = new Request.Builder()
                    .url(apiUrl)
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


                                runOnUiThread(() -> {
                                    CarouselItemFragment fragment = CarouselItemFragment.newInstance(word, symbol, desc, soundUrl);
                                    carouselAdapter.addItem(fragment);
                                });
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(ReciteBookActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }
    }
}