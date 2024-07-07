package com.example.dictionary;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TranslationActivity extends AppCompatActivity {
    private EditText inputEditText;
    private ImageButton from;
    private ImageButton to;
    private ImageButton exchange;
    private ImageButton translate;
    private ImageButton play;
    private TextView fromText;

    private TextView toText;
    private TextView resultText;
    private String fromLan = "zh-CHS";
    private String toLan = "en";
    private OkHttpClient client = new OkHttpClient();
    private String appKey = "284f6f8423c0820f";
    private String appSecret = "kfTTlvnxbtR1w5a18sL9I9snykuYdNPO";
    private String translateUrl = "https://openapi.youdao.com/api";
    private String inputText = "";
    private String resultTranslation;

    protected void onResume() {
        super.onResume();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        BottomLayout bottomLayout = findViewById(R.id.bottom);
        bottomLayout.updateButtonStates("translation");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_translation);

        String salt = UUID.randomUUID().toString();
        resultText = findViewById(R.id.result_text);
        exchange = (ImageButton) findViewById(R.id.ex);
        fromText = findViewById(R.id.from_text);
        toText = findViewById(R.id.to_text);
        inputEditText = findViewById(R.id.edit_text);

        translate = (ImageButton) findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText = inputEditText.getText().toString();
                String url = translateUrl + "?q=" + inputText
                        + "&from=" + fromLan + "&to=" + toLan
                        + "&appKey=" +appKey + "&salt=" + salt + "&sign=" +calculateSign(appKey, inputText, salt)
                        + "&signType=v3&curtime="+ System.currentTimeMillis() / 1000;
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
                            String responseData = response.body().string();
                            if (responseData != null) {
                                try {
                                    Log.d("TranslationActivity", "responseData:"+responseData);
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONArray translationArray = jsonObject.optJSONArray("translation");
                                    for (int i = 0; i < translationArray.length(); i++) {
                                        resultTranslation = translationArray.optString(i);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            resultText.setText(resultTranslation);
                                        }
                                    });
                                } catch(JSONException e){
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Log.e("Translation","responseDta is null!");
                            }
                        }
                    }
                });
            }
        });

        from = (ImageButton) findViewById(R.id.from);
        to = (ImageButton) findViewById(R.id.to);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromPopupMenu();
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToPopupMenu();
            }
        });
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromLanguage = fromText.getText().toString();
                String toLanguage = toText.getText().toString();

                fromText.setText(toLanguage);
                toText.setText(fromLanguage);

                String tempLanguage = fromLan;
                fromLan = toLan;
                toLan = tempLanguage;
            }
        });
    }
    private void showFromPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this,from);
        popupMenu.getMenuInflater().inflate(R.menu.choose, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.english:
                        fromText.setText("英语");
                        fromLan = "en";
                        return true;
                    case R.id.chinese:
                        fromText.setText("中文");
                        fromLan = "zh-CHS";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showToPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this,to);
        popupMenu.getMenuInflater().inflate(R.menu.choose, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.english:
                        toText.setText("英语");
                        toLan = "en";
                        return true;
                    case R.id.chinese:
                        toText.setText("中文");
                        toLan = "zh-CHS";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    private String calculateSign(String appKey, String inputText, String salt) {
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String input;

        if (inputText.length() > 20) {
            input = inputText.substring(0, 10) + inputText.length() + inputText.substring(inputText.length() - 10);
        } else {
            input = inputText;
        }

        String signature = appKey + input + salt + curtime + appSecret;
        return sha256(signature);
    }

    private String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



}