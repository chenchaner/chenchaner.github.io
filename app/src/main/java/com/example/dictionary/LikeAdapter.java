package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{
    private static final int MENU_DELETE = 1;
    private List<Like> mLikeList;
    private List<String> mLetterIndexList;
    private Map<String, List<Like>> mGroupedLikes;
    private List<Like> originalLikeList = new ArrayList<>();
    String ttsUrl;
    private SQLiteDatabase db_like;
    private Context mContext;
    private boolean checkBoxVisible = false;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View likeView;
        TextView word;
        TextView translation;
        ImageButton play;
        CheckBox checkBox;
        public ViewHolder(View view){
            super(view);
            likeView = view;
            word = (TextView) view.findViewById(R.id.like_word);
            translation = (TextView) view.findViewById(R.id.like_translation);
            play = (ImageButton) view.findViewById(R.id.play);
            checkBox = (CheckBox) view.findViewById(R.id.cheekBox);
        }
    }
    public LikeAdapter(Context context, List<Like> likeList){
        mLetterIndexList = new ArrayList<>();
        mGroupedLikes = new HashMap<>();
        mContext = context;
        mLikeList = likeList;
        LikeDatabaseManager likeDatabaseManager = LikeDatabaseManager.getInstance(context);
        db_like = likeDatabaseManager.getDatabase();
        for (Like like : likeList) {
            String firstLetter = like.getWord().substring(0, 1).toUpperCase();
            if (!mLetterIndexList.contains(firstLetter)) {
                mLetterIndexList.add(firstLetter);
            }

            if (mGroupedLikes.containsKey(firstLetter)) {
                mGroupedLikes.get(firstLetter).add(like);
            } else {
                List<Like> newList = new ArrayList<>();
                newList.add(like);
                mGroupedLikes.put(firstLetter, newList);
            }
        }
        Collections.sort(mLetterIndexList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.like_item,parent,false);
        final LikeAdapter.ViewHolder holder = new LikeAdapter.ViewHolder(view);

        holder.likeView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Like like = mLikeList.get(position);
                String searchQuery = like.getWord();
                Intent intent = new Intent(parent.getContext(), WordActivity.class);
                intent.putExtra("search_query", searchQuery);
                parent.getContext().startActivity(intent);
            }
        });
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Like like = mLikeList.get(position);
                ttsUrl = like.getTtsUrl();
                playTts();
                isFormatSupported(ttsUrl);
            }
        });
        holder.likeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Like like = mLikeList.get(position);
                PopupMenu popupMenu = new PopupMenu(parent.getContext(),view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            switch (item.getItemId()) {
                                case R.id.menu_delete:
                                    mLikeList.remove(position); // 从数据集中删除对应项
                                    notifyItemRemoved(position); // 通知适配器已删除某个位置的项
                                    db_like.delete("LikeList", "word=?", new String[]{like.getWord()});
                                    return true;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        return holder;
    }
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Like like = mLikeList.get(position);
        holder.word.setText(like.getWord());
        holder.translation.setText(like.getTranslation());
        Like like1 = mLikeList.get(position);
        holder.checkBox.setText(like1.getWord());
        holder.checkBox.setChecked(like1.isChecked());
        if (checkBoxVisible) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            like.setChecked(isChecked);
        });
    }

    public List<String> getmLetterIndexList() {
        return mLetterIndexList;
    }

    @Override
    public int getItemCount() {
        return mLikeList.size();
    }
    public void updateData(List<Like> filteredList) {
        mLikeList.clear();
        mLikeList.addAll(filteredList);
        notifyDataSetChanged();
    }


    public void showCheckBox() {
        for (Like like : mLikeList) {
            like.setChecked(false);
        }
        notifyDataSetChanged();
        checkBoxVisible = true;
    }

    public void hideCheckBox() {
        checkBoxVisible = false;
        notifyDataSetChanged();
    }


    public Like getItemAtPosition(int position) {
        if (position >= 0 && position < mLikeList.size()) {
            return mLikeList.get(position);
        }
        return null;
    }

    public void sort(){
        Collections.sort(mLikeList, new Comparator<Like>() {
            @Override
            public int compare(Like like1, Like like2) {
                return like1.getFirstLetter().compareTo(like2.getFirstLetter());
            }
        });
        notifyDataSetChanged();
    }

    public void defaultSort(List<Like> originalList){
        mLikeList.clear();
        mLikeList.addAll(originalList);
        notifyDataSetChanged();
    }

    private void playTts() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        try {
            mediaPlayer.setDataSource(ttsUrl);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isFormatSupported(String audioUrl) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.release();
            return true;
        } catch (Exception e) {
            Log.e("deBug","不支持该编码");
            return false;
        }
    }
}
