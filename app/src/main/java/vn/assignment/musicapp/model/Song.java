package vn.assignment.musicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Song implements Serializable {

    int id;
    String song;
    String url;
    String image;
    String artist;
    int views;

    public int getView() {
        return views;
    }

    public void setView(int view) {
        this.views = views;
    }

    public Song() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Song(int id, String song, String url, String image, String artist, int views) {
        this.id = id;
        this.song = song;
        this.url = url;
        this.image = image;
        this.artist = artist;
        this.views = views;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
