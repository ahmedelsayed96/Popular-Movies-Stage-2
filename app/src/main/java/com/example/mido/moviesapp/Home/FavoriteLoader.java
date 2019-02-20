package com.example.mido.moviesapp.Home;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.mido.moviesapp.Movies.MovieProvider;
import com.example.mido.moviesapp.Movies.MoviesData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/09/17.
 */

 class FavoriteLoader extends AsyncTaskLoader<List<MoviesData>> {

    private Context context;
    private List<MoviesData> movies;

    FavoriteLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        movies=new ArrayList<>();
        forceLoad();
    }

    @Override
    public List<MoviesData> loadInBackground() {

            Cursor cursor = context.getContentResolver().query(MovieProvider.MOVIE_URI,
                                                               null,
                                                               null,
                                                               null,
                                                               null
            );
            cursor.moveToFirst();
        Log.e("LoaderCursot",""+cursor.getCount());
            while (!cursor.isAfterLast()) {
                movies.add(new MoviesData(cursor.getString(cursor.getColumnIndex(MovieProvider.MOVIE_IMAGE)),
                                          cursor.getString(cursor.getColumnIndex(MovieProvider.MOVIE_NAME)),
                                            cursor.getInt(cursor.getColumnIndex(MovieProvider.MOVIE_ID)) + ""
                           )
                );
                Log.e("here","-------------------");
                cursor.moveToNext();
            }
            cursor.close();
        Log.e("LoaderList",""+movies.size());
        return movies;
    }

    @Override
    public void deliverResult(List<MoviesData> data) {
        super.deliverResult(data);
    }
}

