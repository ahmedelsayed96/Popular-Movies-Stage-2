package com.example.mido.moviesapp.Home;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.mido.moviesapp.Movies.MoviesData;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mido.moviesapp.Movies.MovieProvider;
import com.example.mido.moviesapp.Movies.MoviesAdapter;
import com.example.mido.moviesapp.R;
import com.example.mido.moviesapp.Utility;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements
                                               LoaderManager.LoaderCallbacks<List<MoviesData>>,
                                               MoviesAdapter.MovieClickListener {

    public static final int LOADER_ID = 12343;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    List<MoviesData> moviesDataList=new ArrayList<>();
    @BindView(R.id.moviesGridView)
    RecyclerView moviesLGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        ButterKnife.bind(this,view);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        return view;
    }


    @Override
    public Loader<List<MoviesData>> onCreateLoader(int id, Bundle args) {
        return new FavoriteLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MoviesData>> loader, List<MoviesData> data) {
        moviesDataList=data;
        Log.e("DataSize",data.size()+"");
       if(moviesDataList.size()!=0){
           MoviesAdapter adapter = new MoviesAdapter(getActivity(),
                                                     moviesDataList,
                                                     FavoriteFragment.this
           );
           int maxNum = Utility.calculateNoOfColumns(getActivity(), 100);
           GridLayoutManager mLayoutManger = new GridLayoutManager(getActivity(), maxNum);
           moviesLGridView.setAdapter(adapter);
           moviesLGridView.setLayoutManager(mLayoutManger);
       }else {
           Toast.makeText(getActivity(),
                          "You Don't Have Movies in Favorite",
                          Toast.LENGTH_SHORT
           ).show();
       }


    }



    @Override
    public void onLoaderReset(Loader<List<MoviesData>> loader) {

    }

    @Override
    public void onMovieItemClickListener(int position, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.movieImage);
        TextView name = (TextView) view.findViewById(R.id.name);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = null;
        byte[] byteArray = null;
        if (drawable != null) {
            bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        Intent intent = new Intent(getActivity(), Details.class);
        Bundle bundle = new Bundle();
        bundle.putByteArray("MovieImage", byteArray);
        bundle.putString("MovieId", moviesDataList.get(position).getId());
        bundle.putString("ImageUrl", moviesDataList.get(position).getPoster_path());
        intent.putExtras(bundle);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> p1 = Pair.create((View) imageView,
                                                imageView.getTransitionName()
            );
            Pair<View, String> p2 = Pair.create((View) name, name.getTransitionName());
            Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                                                          p1,
                                                                          p2
            ).toBundle();
            startActivityForResult(intent, 1, bundle1);
        } else {
            startActivity(intent);

        }

    }

}
