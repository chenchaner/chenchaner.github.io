package com.example.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class CarouselItemFragment extends Fragment {
    private String word;
    private String pronunciation;
    private String translation;
    private String ttsUrl;
    private SQLiteDatabase db_like;
    private Drawable BeforeIcon;
    private Drawable AfterIcon;

    public static CarouselItemFragment newInstance(String word, String pronunciation, String translation, String ttsUrl) {
        CarouselItemFragment fragment = new CarouselItemFragment();
        Bundle args = new Bundle();
        args.putString("word", word);
        args.putString("pronunciation", pronunciation);
        args.putString("translation", translation);
        args.putString("ttsUrl", ttsUrl);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            word = getArguments().getString("word");
            pronunciation = getArguments().getString("pronunciation");
            translation = getArguments().getString("translation");
            ttsUrl = getArguments().getString("ttsUrl");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BeforeIcon = getResources().getDrawable(R.drawable.like_before);
        AfterIcon = getResources().getDrawable(R.drawable.like_after);
        MediaPlayer mediaPlayer = new MediaPlayer();
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("ttsUrl",ttsUrl);
        values.put("pronunciation",pronunciation);
        values.put("translation",translation);

        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(requireContext());
        db_like = likeDatabaseManager.getDatabase();
        View rootView = inflater.inflate(R.layout.carousel_item, container, false);
        TextView wordTextView = rootView.findViewById(R.id.word);
        TextView pronunciationTextView = rootView.findViewById(R.id.pronunciation);
        TextView translationTextView = rootView.findViewById(R.id.translation_recite);
        ImageButton like = rootView.findViewById(R.id.like_recite);
        ImageButton playButton = rootView.findViewById(R.id.play_recite);
        wordTextView.setText(word);
        pronunciationTextView.setText(pronunciation);
        translationTextView.setText(translation);
        Cursor cursor = db_like.rawQuery("SELECT * FROM LikeList WHERE word = ?", new String[]{word});
        if (cursor != null && cursor.moveToFirst()){
            like.setImageDrawable(AfterIcon);
        }else {
            like.setImageDrawable(BeforeIcon);
        }
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (ttsUrl != null && !ttsUrl.isEmpty()) {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(ttsUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }else {
                        Toast.makeText(getContext(), "No audio URL available", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error playing audio", Toast.LENGTH_SHORT).show();
                }

            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable currentIcon = like.getDrawable();
                if (currentIcon.getConstantState().equals(BeforeIcon.getConstantState())) {
                    like.setImageDrawable(AfterIcon);
                    db_like.insert("LikeList",null,values);
                    Log.d("WordActivity","values:"+values);
                } else if (currentIcon.getConstantState().equals(AfterIcon.getConstantState())) {
                    like.setImageDrawable(BeforeIcon);
                    db_like.delete("LikeList", "word=?", new String[]{word});
                } else {
                    like.setImageDrawable(BeforeIcon);
                }
            }
        });
        return rootView;
    }
}

