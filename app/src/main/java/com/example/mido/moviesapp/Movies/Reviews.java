package com.example.mido.moviesapp.Movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class Reviews {

    String id;
    @SerializedName("results")
    private List<Review> reviews;

    public Reviews() {
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
