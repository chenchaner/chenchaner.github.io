package com.example.dictionary.Translation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.dictionary.Base.BaseModel;
import com.example.dictionary.Interface.ITranslation;

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

public class TranslationModel extends BaseModel<TranslationPresenter, ITranslation.M> {

    private String translation;
    private String apiUrl;
    String salt;
    private String appKey = "284f6f8423c0820f";
    private String appSecret = "kfTTlvnxbtR1w5a18sL9I9snykuYdNPO";
    public TranslationModel(TranslationPresenter mPresenter){
        super(mPresenter);
    }

    @Override
    public ITranslation.M getContract() {
        return new ITranslation.M() {
            @Override
            public void requestNetwork(String url,String inputText) {
                salt = UUID.randomUUID().toString();
                apiUrl = url + "&appKey=" +appKey + "&salt=" + salt + "&sign=" +calculateSign(appKey, inputText, salt)
                        + "&signType=v3&curtime="+ System.currentTimeMillis() / 1000;
                Log.d("tran-debug", "tran"+apiUrl);
                fetchData(apiUrl);
                mPresenter.getContract().responseForNetwork(translation);
            }
        };
    }
    public void fetchData(String apiUrl){
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();
        OkHttpClient client = new OkHttpClient();
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
                                translation = translationArray.optString(i);
                            }
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
