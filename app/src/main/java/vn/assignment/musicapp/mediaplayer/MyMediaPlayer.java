package vn.assignment.musicapp.mediaplayer;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer mediaPlayer;

    public static MediaPlayer getInstance(){
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    public static int currentSong = -1;
}
