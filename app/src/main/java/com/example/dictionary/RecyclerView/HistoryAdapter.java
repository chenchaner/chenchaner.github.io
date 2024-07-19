package com.example.dictionary.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.DataBase.HistoryDatabaseManager;
import com.example.dictionary.R;
import com.example.dictionary.Word.WordActivity;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<History> mHistoryList;
    private SQLiteDatabase db_history;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View historyView;
        TextView historyEnglish;
        public ViewHolder(View view){
            super(view);
            historyView = view;
            historyEnglish = (TextView) view.findViewById(R.id.english_translation);
        }
    }
    
    public HistoryAdapter(Context context, List<History> historyList){
        mContext = context;
        mHistoryList = historyList;
        HistoryDatabaseManager historyDatabaseManager = HistoryDatabaseManager.getInstance(context);
        db_history = historyDatabaseManager.getDatabase();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.historyView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                History history = mHistoryList.get(position);
                String searchQuery = history.getEnglish();
                Intent intent = new Intent(parent.getContext(), WordActivity.class);
                intent.putExtra("search_query", searchQuery);
                parent.getContext().startActivity(intent);
            }
        });
        holder.historyView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                History history = mHistoryList.get(position);
                PopupMenu popupMenu = new PopupMenu(parent.getContext(),view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            switch (item.getItemId()) {
                                case R.id.menu_delete:
                                    mHistoryList.remove(position);
                                    notifyItemRemoved(position);
                                    db_history.delete("History", "english=?", new String[]{history.getEnglish()});
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = mHistoryList.get(position);
        holder.historyEnglish.setText(history.getEnglish());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
    public void updateList(List<History> newList) {
        mHistoryList = newList;
        notifyDataSetChanged();
    }
}
