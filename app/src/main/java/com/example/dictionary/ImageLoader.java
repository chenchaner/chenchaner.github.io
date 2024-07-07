package com.example.dictionary;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

    public ImageLoader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return BitmapFactory.decodeStream(response.body().byteStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int targetWidth = (int) (screenWidth * 0.8);
            int targetHeight = (int) (bitmap.getHeight() * (targetWidth / (float) bitmap.getWidth()));
            imageView.getLayoutParams().width = targetWidth;
            imageView.getLayoutParams().height = targetHeight;
        }
    }
}
