package vn.assignment.musicapp.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.SongRecycleViewAdapter;
import vn.assignment.musicapp.model.Song;

public class LocalSongActivty extends AppCompatActivity {
    RecyclerView recyclerViewLocal;
    TextView noMusicTextView;
    ArrayList<Song> songsList = new ArrayList<>();
    ListView listView;
    String[] items;
    private SongRecycleViewAdapter songRecycleViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song_activty);
        recyclerViewLocal = findViewById(R.id.recyclerViewLocal);
        runtimePermission();



//        noMusicTextView = findViewById(R.id.no_songs_text);


    }
    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displaySong();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(findSong(singlefile));
            }else{
                if(singlefile.getName().endsWith(".mp3")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void displaySong(){
        String path = Environment.getExternalStorageDirectory().toString()+"/Download" + "/Song";
        File directory = new File(path);
        final ArrayList<File> mySongs = findSong(directory);


        Log.d("Files", "Path: " + Environment.DIRECTORY_DOWNLOADS);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        items = new String[mySongs.size()];
        for(int i=0;i<mySongs.size();i++){
            String url = mySongs.get(i).getPath();
            mmr.setDataSource(url);
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            Song song = new Song();
            song.setSong(title);
            song.setArtist(artist);
            song.setUrl(url);
            songsList.add(song);

        }
        loadSongList(songsList);
    }

    private void loadSongList(List<Song> listSong) {

        songRecycleViewAdapter = new SongRecycleViewAdapter(listSong,LocalSongActivty.this);
        songRecycleViewAdapter.setPrevActivity("LOCAL");
        recyclerViewLocal.setLayoutManager(new GridLayoutManager(LocalSongActivty.this,1));
        recyclerViewLocal.setAdapter(songRecycleViewAdapter);
    }
}