package vn.assignment.musicapp.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.mediaplayer.MyMediaPlayer;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;

public class PlayerSongActivity extends AppCompatActivity {
    ImageView img;
    ImageButton play,prev,next,loop,suffer;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    TextView tvTitle, tvArtist;
    boolean isSuffer = true;
    String  song,url,image,artist;
    boolean isPlay = true, isLoop = false;
    private Handler handler = new Handler();
    private TextView tvCurrentTime,tvTotalTime, textViewOptions;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    List<Song> songList;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownLoad();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_song);
        // connect to component
        initView();
        //get media player
        mediaPlayer = MyMediaPlayer.getInstance();
        // set action when click download in drop down list
        textViewOptions.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(PlayerSongActivity.this, textViewOptions);
            popupMenu.inflate(R.menu.option_item_song_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.download:
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_DENIED){
                                    // không cho phép tải

                                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                                    requestPermissions(permission, PERMISSION_STORAGE_CODE);
                                }else{

                                    startDownLoad();
                                }
                            }else{
                                startDownLoad();
                            }

                    }
                    return false;
                }
            });
            popupMenu.show();

        });
        // get Data from previous Activity
        getDataFromPreviousActivity();




        // prepare media player
        prepareMediaPlayer();


        spinImage();
        // set up seeker bar
        loop.setOnClickListener(view -> {
            if(mediaPlayer.isLooping()){
                loop.setColorFilter(Color.argb(255,99,96,96));
                mediaPlayer.setLooping(false);
            }else{
                loop.setColorFilter(Color.argb(255,255,255,255));
                mediaPlayer.setLooping(true);
            }

        });
        seekBar.setMax(100);
        togglePlayButton();
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SeekBar seekBar = (SeekBar) view;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                tvCurrentTime.setText(changeSeconds(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try{
                    if(!mediaPlayer.isLooping())
                        playNextSong();

                }catch (Exception e){
                    e.printStackTrace();

                }
            }
        });
        prev.setOnClickListener(view -> {
            playPreviousSong();
        });
        suffer.setOnClickListener(view -> {
            if(isSuffer){
                isSuffer = false;
                suffer.setColorFilter(Color.argb(255,99,96,96));
            }else{
                isSuffer = true;
                suffer.setColorFilter(Color.argb(255,255,255,255));
            }
        });
        next.setOnClickListener(view -> {
            playNextSong();
        });
        runSeekerBar();
    }



    private void startDownLoad() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);



        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,File.separator + "Song" + File.separator + song+".mp3");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            runSeekerBar();
            long currentTime = mediaPlayer.getCurrentPosition();
            tvCurrentTime.setText(changeSeconds(currentTime));
        }
    };
    private void runSeekerBar(){
        float t =(float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        if(mediaPlayer.isPlaying()){
            seekBar.setProgress((int)( t * 100 ));
            handler.postDelayed(updater, 1000);
        }
    }
    private void initView() {
        loop = findViewById(R.id.loopButton);
        img = findViewById(R.id.imageSongPlayer);
        tvTitle = findViewById(R.id.playerTitle);
        tvArtist = findViewById(R.id.playerArtist);
        play = findViewById(R.id.btnPlay);
        tvCurrentTime = findViewById(R.id.currentTime);
        tvTotalTime= findViewById(R.id.totalTime);
        seekBar = findViewById(R.id.seek_bar);
        textViewOptions = findViewById(R.id.textViewOptions);
        prev = findViewById(R.id.btnPrev);
        next = findViewById(R.id.btnNext);
        suffer = findViewById(R.id.btnSuffer);
    }

    private void takeCurrentData(){
        Song currentSong = songList.get(MyMediaPlayer.currentSong);

        url = currentSong.getUrl();
        image = currentSong.getImage();
        song = currentSong.getSong();
        artist = currentSong.getArtist();
    }
    private void prepareMediaPlayer() {
        try {
            takeCurrentData();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            tvTotalTime.setText(changeSeconds(mediaPlayer.getDuration()));
            // Set title and artist name
            tvTitle.setText(song);
            tvArtist.setText(artist);
            setupImage();
            mediaPlayer.start();

        }catch (IOException e){
            e.printStackTrace();
            finish();
            Toast.makeText(this, "Lỗi mạng không ổn định", Toast.LENGTH_SHORT).show();
        }
    }
    private void togglePlayButton() {
        play.setOnClickListener(view -> {
            if(isPlay){
                pauseMusic();

            }
            else {
                playMusic();

            }
        });
    }
    private void pauseMusic() {
        play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        isPlay = false;
        mediaPlayer.pause();
        img.animate().cancel();
        handler.removeCallbacks(updater);
    }
    private void playMusic() {
        play.setImageResource(R.drawable.ic_baseline_pause_24);
        isPlay = true;
        mediaPlayer.start();
        spinImage();
        runSeekerBar();
    }
    private void playNextSong(){

        if(MyMediaPlayer.currentSong == songList.size() - 1) {
            MyMediaPlayer.currentSong = -1;
        }
        MyMediaPlayer.currentSong += 1;
       
        if(isSuffer){
            Random random = new Random();
            int position = random.nextInt(songList.size());
            MyMediaPlayer.currentSong = position;
        }
        mediaPlayer.reset();
        prepareMediaPlayer();

    }
    private void playPreviousSong(){

        if(MyMediaPlayer.currentSong == 0) return;
        MyMediaPlayer.currentSong -= 1;
        mediaPlayer.reset();
        prepareMediaPlayer();

    }
    private void getDataFromPreviousActivity() {
        Intent i = getIntent();
        Bundle args = getIntent().getBundleExtra("bundle");
        songList = ( List<Song>) args.getSerializable("listSong");

    }
    private void setupImage() {
        Glide.with(this).load(image).error(R.drawable.anh_dep).into(img);

    }
    private void spinImage() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                img.animate().rotationBy(360).withEndAction(this)
                        .setDuration(20000)
                        .setInterpolator(new LinearInterpolator())

                        .start();

            }
        };

            img.animate().rotationBy(360).withEndAction(runnable).setDuration(20000).setInterpolator(new LinearInterpolator()).start();


        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.pause();
        finish();
    }

    private String changeSeconds(long milliSeconds){
        String timerString = "";
        String secondString;

        int hours = (int) (milliSeconds / (1000*60*60));
        int minutes = (int)(milliSeconds % (1000*60*60)) / (1000*60);
        int second = (int) ((milliSeconds % (1000*60*60)) % (1000*60)/1000 );

        if(hours > 0){
            timerString  = hours + ":";
        }
        if(second < 10){
            secondString = "0" + second;

        }else{
            secondString = "" + second;
        }
        timerString = timerString + minutes + ":" +secondString;
        return timerString;
    }





}