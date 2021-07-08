package com.carbon.test.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.carbon.test.common.interfaces.RecyclerViewItem;
import com.carbon.test.constant.Const;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "movies")
public class Movie implements RecyclerViewItem, Serializable {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    private int movieID;

    @ColumnInfo(name = "title", index = true)
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "vote")
    @SerializedName("vote_average")
    private float vote;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;

    @Ignore
    public Movie() {

    }

    public Movie(int movieID, String title, String releaseDate, String overview, String posterPath) {
        this.movieID = movieID;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVote() {
        return vote;
    }

    public void setVote(float vote) {
        this.vote = vote;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPosterUrl() {
        return Const.POSTER_URL + getPosterPath();
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int getItemType() {
        return VIEW_TYPE_ITEM;
    }
}
