package com.example.mido.moviesapp.Movies.Cast;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mido.moviesapp.Movies.Review;
import com.example.mido.moviesapp.R;

import java.util.List;



public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(@NonNull Context context,
                         @NonNull List<Review> objects
    ) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent
    ) {
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.review_templet,parent,false);
        }
        TextView author=(TextView)view.findViewById(R.id.author);
        TextView content=(TextView)view.findViewById(R.id.content);
        Review review=getItem(position);
        author.setText(review.getAuthor());
        content.setText(review.getContent());
        return view;
    }
}
