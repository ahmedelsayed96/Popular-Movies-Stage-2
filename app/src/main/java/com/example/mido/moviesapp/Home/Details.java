package com.example.mido.moviesapp.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mido.moviesapp.Movies.Cast.CastRecyclerAdapter;
import com.example.mido.moviesapp.Movies.Cast.Cast;
import com.example.mido.moviesapp.Movies.Cast.ReviewAdapter;
import com.example.mido.moviesapp.Movies.MovieProvider;
import com.example.mido.moviesapp.Movies.Movies;
import com.example.mido.moviesapp.Movies.MoviesClient;
import com.example.mido.moviesapp.Movies.MoviesData;
import com.example.mido.moviesapp.Movies.Review;
import com.example.mido.moviesapp.Movies.Reviews;
import com.example.mido.moviesapp.Movies.Videos;
import com.example.mido.moviesapp.R;
import com.example.mido.moviesapp.Utility;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Details extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    public static final String IMAGE_ROOT = "https://image.tmdb.org/t/p/w500";
    String id;


    private MoviesClient movieClient;
    private MoviesData moviesData;
    private String genreS;
    Bitmap imageBitmap;
    boolean isFavorite = false;
    RecyclerView castView;
    @BindView(R.id.movieRate)
    RatingBar rate;
    @BindView(R.id.mainImage)
    ImageView image;
    @BindView(R.id.movieName)
    TextView name;
    @BindView(R.id.rateText)
    TextView rateText;
    @BindView(R.id.rateVoted)
    TextView rateNum;
    @BindView(R.id.Genre)
    TextView genre;
    @BindView(R.id.Adult)
    TextView adult;
    @BindView(R.id.ReleaseDate)
    TextView date;
    @BindView(R.id.Status)
    TextView status;
    @BindView(R.id.Budget)
    TextView budget;
    @BindView(R.id.Runtime)
    TextView runtime;
    @BindView(R.id.OriginalLanguage)
    TextView originalLang;
    @BindView(R.id.Homepage)
    TextView homePage;
    @BindView(R.id.Overview)
    TextView overView;
    @BindView(R.id.YoutubeView)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.favorite_image)
    ImageView favoriteImage;
    private List<Review> reviews;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        id = getIntent().getExtras().getString("MovieId");
        byte[] bytes = getIntent().getExtras().getByteArray("MovieImage");
        ButterKnife.bind(this);
        if (bytes != null) {
            imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            BitmapDrawable drawable = new BitmapDrawable(getResources(), imageBitmap);
            image.setBackgroundDrawable(drawable);

        } else {
            Picasso.with(this).load(IMAGE_ROOT + getIntent().getExtras().getString("ImageUrl")).into(
                    image);

        }
        getDataFromWeb();

    }

    /**
     * get Movie Data And display it into UI
     */

    void getDataFromWeb() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieClient = retrofit.create(MoviesClient.class);
        movieClient.getMovie(id,
                             getString(R.string.movie_api_key),
                             PopularFragment.DEFAULT_LANGUAGE
        ).enqueue(new Callback<MoviesData>() {
            @Override
            public void onResponse(@NonNull Call<MoviesData> call,
                                   @NonNull Response<MoviesData> response
            ) {
                moviesData = response.body();

                setMovieData();
                getCastData();
                getMovieReviews();
                getYouTubeLink();

            }

            @Override
            public void onFailure(Call<MoviesData> call, Throwable t) {

            }
        });


    }

    /**
     * get Movie Videos And set First Video in YoutubeView
     */
    private void getYouTubeLink() {
        movieClient.getVideos(id,
                              getString(R.string.movie_api_key),
                              PopularFragment.DEFAULT_LANGUAGE
        ).enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response

            ) {

                if (response.code() == 200) {
                    setYouTubeView(response.body().getVideos().get(0).getKey());
                }

            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }

    void setYouTubeView(final String key) {
        this.key = key;
        Log.e("Key,", key);
        youTubePlayerView.initialize(getString(R.string.youtube_api_key), this);
    }

    /**
     * get Cast  and set it to Recycler to be Displayed
     */
    void getCastData() {
        movieClient.getCast(id, getString(R.string.movie_api_key))
                   .enqueue(new Callback<Cast>() {
                       @Override
                       public void onResponse(@NonNull Call<Cast> call,
                                              @NonNull Response<Cast> response
                       ) {
                           if (response.code() == PopularFragment.RESULT_OK) {
                               Cast cast = response.body();
                               castView = (RecyclerView) findViewById(R.id.Cast);
                               CastRecyclerAdapter adapter = new CastRecyclerAdapter(Details.this,
                                                                                     cast.getCast()
                               );
                               castView.setAdapter(adapter);
                               castView.setLayoutManager(new LinearLayoutManager(Details.this,
                                                                                 LinearLayout.HORIZONTAL,
                                                                                 false
                               ));
                           }
                       }

                       @Override
                       public void onFailure(Call<Cast> call, Throwable t) {
                           Log.e("CastError", t.getMessage());
                       }
                   });

    }

    /**
     * get Movie Review and display it to ListView
     */
    void getMovieReviews() {
        movieClient.getMovieReviews(id,
                                    getString(R.string.movie_api_key),
                                    PopularFragment.DEFAULT_LANGUAGE,
                                    "1"
        )
                   .enqueue(new Callback<Reviews>() {
                       @Override
                       public void onResponse(@NonNull Call<Reviews> call,
                                              @NonNull Response<Reviews> response
                       ) {
                           if (response.code() == PopularFragment.RESULT_OK) {
                               findViewById(R.id.show_reviews).setVisibility(View.VISIBLE);
                               reviews = response.body().getReviews();
                           }
                       }

                       @Override
                       public void onFailure(Call<Reviews> call, Throwable t) {
                           Log.e("ReviewError", t.getMessage());
                       }
                   });
    }

    /**
     * set movie data to UI View
     * and check if that movie in Database or not
     */
    private void setMovieData() {
        String adultS;
        if (moviesData.getAdult().equals("true")) {
            adultS = "Yes";
        } else {
            adultS = "No";
        }
        double runTime = Double.parseDouble(moviesData.getRuntime());
        double inMin = (runTime / 60);
        String runtimeS = ((int) inMin) + "h " + (int) ((inMin - ((int) inMin)) * 60) + "m";

        for (int i = 0; i < moviesData.getGenres().size(); i++) {

            MoviesData.Genres genres = moviesData.getGenres().get(i);
            if (i == 0) {
                genreS = genres.getName();
            } else {
                String s = "/" + genres.getName();
                genreS = genreS.concat(s);
            }
        }
        rate.setRating(Float.parseFloat(moviesData.getVote_average()) / 2);
        name.setText(moviesData.getOriginal_title());
        rateText.setText("" + Double.parseDouble(moviesData.getVote_average()) / 2);
        rateNum.setText(moviesData.getVote_count() + " voted");
        genre.setText(genreS);
        date.setText(moviesData.getRelease_date());
        runtime.setText(runtimeS);
        originalLang.setText(moviesData.getOriginal_language());
        overView.setText(moviesData.getOverview());
        budget.setText(moviesData.getBudget());
        homePage.setText(moviesData.getHomepage());
        adult.setText(adultS);
        status.setText(moviesData.getStatus());
        findViewById(R.id.genre_text).setMinimumWidth(Utility.calculateScreenWidthDividedBy2(this));
        // see if Db contain movie or Not
        Cursor cursor = getContentResolver().query(MovieProvider.MOVIE_URI,
                                                   null,
                                                    MovieProvider.MOVIE_ID + "=?",
                                                   new String[]{moviesData.getId()},
                                                   null
        );
        isFavorite = cursor.getCount() != 0;
        if (isFavorite) {
            favoriteImage.setBackgroundResource(R.drawable.ic_favorite);
        }else {
            favoriteImage.setBackgroundResource(R.drawable.ic_favorite_border);
        }
        cursor.close();


    }


    public void saveToDb(View view) {
        if (!isFavorite && moviesData != null) {
            ContentValues values = new ContentValues();
            values.put(MovieProvider.MOVIE_ID, Integer.parseInt(moviesData.getId()));
            values.put(MovieProvider.MOVIE_NAME, moviesData.getOriginal_title());
            values.put(MovieProvider.MOVIE_IMAGE, moviesData.getPoster_path());
            Uri returnUri = getContentResolver().insert(MovieProvider.MOVIE_URI, values);
            if (returnUri == null) {
                Toast.makeText(this, "Error! adding to favorite ", Toast.LENGTH_SHORT).show();
            } else {
                favoriteImage.setBackgroundResource(R.drawable.ic_favorite);
            }
        } else {
           int result= getContentResolver().delete(MovieProvider.MOVIE_URI,
                                          MovieProvider.MOVIE_ID + "=?",
                                        new String[]{moviesData.getId()});
            if(result!=0){
                favoriteImage.setBackgroundResource(R.drawable.ic_favorite_border);
            }else {
                Toast.makeText(this, "Error! removing from favorite ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showReviews(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.review);
        ListView reviewsListView
                = (ListView) dialog.findViewById(R.id.reviewList);
        reviewsListView.setAdapter(new ReviewAdapter(this, reviews));
        dialog.show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer,
                                        boolean b
    ) {
        if (youTubePlayer == null) return;

        if (!b) {
            Log.e("Youttube", key);
            youTubePlayer.cueVideo(key);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult
    ) {
        Toast.makeText(this,
                       "There is Problem With YouTube App in your Device",
                       Toast.LENGTH_SHORT
        ).show();
        Log.e("ErrorYouttube", youTubeInitializationResult.toString());
    }
}
