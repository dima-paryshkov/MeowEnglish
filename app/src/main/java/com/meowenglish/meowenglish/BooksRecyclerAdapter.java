package com.meowenglish.meowenglish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meowenglish.meowenglish.data.Book;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.ViewHolder>
{
    private ArrayList<Book> books;
    private Context context;


    public  BooksRecyclerAdapter(Context context, ArrayList<Book> books)
    {
        this.context = context;
        this.books = books;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.book_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.bookCoverImage.setImageBitmap(convertBytesToBitmap(book.getCoverImage()));
        holder.bookTitle.setText(book.getTitle());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    private Bitmap convertBytesToBitmap(byte[] bytes)
    {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private byte[] convertBitmapToBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView bookCoverImage;
        TextView bookTitle;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bookCoverImage = itemView.findViewById(R.id.bookCoverImage);
            bookTitle = itemView.findViewById(R.id.bookTitle);
        }
    }
}