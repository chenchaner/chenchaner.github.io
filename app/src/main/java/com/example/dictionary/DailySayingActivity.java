package com.example.dictionary;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DailySayingActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView daily;
    private TextView sentence;
    private TextView date;
    private TextView translation;
    private String ttsUrl;
    private ImageButton search;

    protected void onResume() {
        super.onResume();
        BottomLayout bottomLayout = findViewById(R.id.bottom);
        bottomLayout.updateButtonStates("search");
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailysaying);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        date = findViewById(R.id.date);
        daily = findViewById(R.id.daily_pic);
        sentence = findViewById(R.id.sentence);
        translation = findViewById(R.id.translation);
        search = (ImageButton) findViewById(R.id.search_button);

        if (savedInstanceState != null) {
            int savedVisibility = savedInstanceState.getInt("daily_visibility", View.VISIBLE);
            daily.setVisibility(savedVisibility);
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailySayingActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        ImageButton play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTts();
            }
        });
        final View parent = (View) play.getParent();
        parent.post(new Runnable() {
            @Override
            public void run() {
                final Rect delegateArea = new Rect();
                play.getHitRect(delegateArea);
                delegateArea.top -= 100;
                delegateArea.bottom += 100;
                delegateArea.left -= 100;
                delegateArea.right += 100;

                TouchDelegate touchDelegate = new TouchDelegate(delegateArea, play);

                if (View.class.isInstance(play.getParent())) {
                    ((View) play.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });

        String apiUrl = "http://open.iciba.com/dsapi";

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(apiUrl)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            String imageUrl = jsonObject.optString("picture");
                            String TextSentence = jsonObject.optString("content");
                            String TextTranslation = jsonObject.optString("note");
                            String TextDate = jsonObject.optString("dateline");
                            ttsUrl = jsonObject.optString("tts");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageLoader imageLoader = new ImageLoader(daily);
                                    imageLoader.execute(imageUrl);
                                    sentence.setText(TextSentence);
                                    translation.setText(TextTranslation);
                                    date.setText(TextDate);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("daily_visibility", daily.getVisibility());
    }


    private void playTts() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build());
        } else {
            mediaPlayer.reset(); // 重置MediaPlayer状态
        }

        try {
            mediaPlayer.setDataSource(ttsUrl);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 播放完成后释放资源
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
