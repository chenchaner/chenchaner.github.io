package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class BottomLayout extends LinearLayout {
    ImageButton search;
    ImageButton translation;
    ImageButton notebook;
    ImageButton recite;
    public BottomLayout (Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom, this);
        search = (ImageButton) findViewById(R.id.search);
        translation = (ImageButton) findViewById(R.id.translation);
        notebook = (ImageButton) findViewById(R.id.notebook);
        recite = (ImageButton) findViewById(R.id.recite);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DailySayingActivity.class);
                getContext().startActivity(intent);
            }
        });
        translation.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TranslationActivity.class);
                getContext().startActivity(intent);
            }
        });
        notebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LikeActivity.class);
                getContext().startActivity(intent);
            }
        });
        recite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BookSearchActivity.class);
                getContext().startActivity(intent);
            }
        });
    }

        public void updateButtonStates(String activeButton) {
            switch (activeButton) {
                case "search":
                    search.setImageResource(R.drawable.search_after);
                    translation.setImageResource(R.drawable.translation_before);
                    notebook.setImageResource(R.drawable.notebook_before);
                    recite.setImageResource(R.drawable.recite_before);
                    break;
                case "translation":
                    search.setImageResource(R.drawable.search_before);
                    translation.setImageResource(R.drawable.translation_after);
                    notebook.setImageResource(R.drawable.notebook_before);
                    recite.setImageResource(R.drawable.recite_before);
                    break;
                case "notebook":
                    search.setImageResource(R.drawable.search_before);
                    translation.setImageResource(R.drawable.translation_before);
                    notebook.setImageResource(R.drawable.notebook_after);
                    recite.setImageResource(R.drawable.recite_before);
                    break;
                case "recite":
                    search.setImageResource(R.drawable.search_before);
                    translation.setImageResource(R.drawable.translation_before);
                    notebook.setImageResource(R.drawable.notebook_before);
                    recite.setImageResource(R.drawable.recite_after);
                    break;
            }
    }
}
