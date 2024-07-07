package com.example.dictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.ViewHolder>{private List<History> mHistoryList;
    private AdapterView.OnItemClickListener listener;
    private List<Translation> mTranslationList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView part;
        TextView mean;
        public ViewHolder(View view){
            super(view);
            part = (TextView) view.findViewById(R.id.part);
            mean = (TextView) view.findViewById(R.id.mean);
        }
    }

    public TranslationAdapter(List<Translation> translationList){
        mTranslationList = translationList;
    }

    @NonNull
    @Override
    public TranslationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.translation_item,parent,false);
        TranslationAdapter.ViewHolder holder = new TranslationAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TranslationAdapter.ViewHolder holder, int position) {
        Translation translation = mTranslationList.get(position);
        holder.part.setText(translation.getPart());
        holder.mean.setText(translation.getMean());
    }

    @Override
    public int getItemCount() {
        return mTranslationList.size();
    }
}
