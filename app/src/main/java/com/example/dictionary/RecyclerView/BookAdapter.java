package com.example.dictionary.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;
import com.example.dictionary.ReciteBook.ReciteBookActivity;

import java.util.List;
import java.util.Random;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    public List<Book> mBookList;
    private Context mContext;
    private int[] ImageArray = new int[]{R.drawable.bac_a,R.drawable.bac_b,R.drawable.bac_c,R.drawable.bac_d,R.drawable.bac_e,R.drawable.bac_f,R.drawable.bac_g,R.drawable.bac_h};
    public class ViewHolder extends RecyclerView.ViewHolder{
        View bookView;
        TextView BookName;
        TextView WordsNumber;
        ImageButton study;
        CardView cardView;
        public ViewHolder (View view){
            super(view);
            bookView = view;
            BookName = (TextView) view.findViewById(R.id.book_name);
            WordsNumber = (TextView) view.findViewById(R.id.word_number);
            study = (ImageButton) view.findViewById(R.id.study);
            cardView = (CardView) view.findViewById(R.id.card);
        }
    }
    public BookAdapter(Context context,List<Book> bookList){
        this.mContext = context;
        this.mBookList = bookList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                holder.study.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Book book = mBookList.get(position);
                        String id = book.getBookId();
                        String courseNumber = book.getCourseNumber();
                        Intent intent = new Intent(mContext, ReciteBookActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("courseNumber",courseNumber);
                        mContext.startActivity(intent);
                    }
                });
            }
        });
        int randomIndex = new Random().nextInt(ImageArray.length);
        int imageResId = ImageArray[randomIndex];
        Drawable drawable = ContextCompat.getDrawable(mContext, imageResId);
        holder.cardView.setBackground(drawable);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.WordsNumber.setText(book.getNumber());
        holder.BookName.setText(book.getName());
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
