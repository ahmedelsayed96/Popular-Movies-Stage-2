package com.example.mido.moviesapp.Movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Videos {
    String id;
    @SerializedName("results")
    private
    List<Video> videos;


    public List<Video> getVideos() {
        return videos;
    }

    public class Video{
        String key;

        public String getKey() {
            return key;
        }
    }

}
