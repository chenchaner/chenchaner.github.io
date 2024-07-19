package com.example.dictionary.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;

import java.util.List;

public class LetterIndexAdapter extends  RecyclerView.Adapter<LetterIndexAdapter.ViewHolder> {
    private List<String> mLetterIndexList;
    private RecyclerView mLikeRecyclerView;
    private List<Like> mLikeList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView letter;
        public ViewHolder(View view){
            super(view);
            letter = (TextView) view.findViewById(R.id.letter);
        }
    }
    public LetterIndexAdapter(List<String> letterIndexList,List<Like> likeList){
        this.mLetterIndexList = letterIndexList;
        this.mLikeList = likeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.letterindex_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String letter = mLetterIndexList.get(position);
        holder.letter.setText(letter);
        holder.letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clickedLetter = mLetterIndexList.get(holder.getAdapterPosition());
                int scrollToPosition = findScrollPosition(clickedLetter);
                if (scrollToPosition != -1 && mLikeRecyclerView != null) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mLikeRecyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        layoutManager.scrollToPositionWithOffset(scrollToPosition, 0);
                    }
                }
            }
        });
    }
    public void setLikeRecyclerView(RecyclerView recyclerView) {
        mLikeRecyclerView = recyclerView;
    }

    private int findScrollPosition(String letter) {
        for (int i = 0; i < mLikeList.size(); i++) {
            Like like = mLikeList.get(i);
            if (like.getFirstLetter().equalsIgnoreCase(letter)) {
                return i;
            }
        }
        return -1;
    }

    public int getItemCount() {
        return mLetterIndexList.size();
    }
}
