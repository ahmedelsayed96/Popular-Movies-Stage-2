package com.example.mido.moviesapp.Movies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ahmed
 */

public class MoviesData implements Parcelable{
    private String poster_path,
            adult,
            overview,
            release_date,
            original_title,
            id,
            original_language,
            backdrop_path,
            popularity,
            vote_count,
            vote_average,
            homepage,
            budget,
            status,
            runtime;
    private List<Genres> genres;

    public MoviesData(String poster_path, String original_title, String id) {
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.id = id;
    }

    protected MoviesData(Parcel in) {
        poster_path = in.readString();
        adult = in.readString();
        overview = in.readString();
        release_date = in.readString();
        original_title = in.readString();
        id = in.readString();
        original_language = in.readString();
        backdrop_path = in.readString();
        popularity = in.readString();
        vote_count = in.readString();
        vote_average = in.readString();
        homepage = in.readString();
        budget = in.readString();
        status = in.readString();
        runtime = in.readString();
    }

    public static final Creator<MoviesData> CREATOR = new Creator<MoviesData>() {
        @Override
        public MoviesData createFromParcel(Parcel in) {
            return new MoviesData(in);
        }

        @Override
        public MoviesData[] newArray(int size) {
            return new MoviesData[size];
        }
    };

    public List<Genres> getGenres() {
        return genres;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getBudget() {
        return budget;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public String getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(adult);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(original_title);
        parcel.writeString(id);
        parcel.writeString(original_language);
        parcel.writeString(backdrop_path);
        parcel.writeString(popularity);
        parcel.writeString(vote_count);
        parcel.writeString(vote_average);
        parcel.writeString(homepage);
        parcel.writeString(budget);
        parcel.writeString(status);
        parcel.writeString(runtime);
    }

    public class Genres{
        String name;

        public String getName() {
            return name;
        }
    }
}
