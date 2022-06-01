package vn.assignment.musicapp.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.SongRecycleViewAdapter;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;

public class ArtistListActivity extends AppCompatActivity {
    ImageView imgArtist;
    TextView tvNameArtist;
    private Context context;
    RecyclerView recyclerViewArtist;
    List<Song> listSong;
    String name,image;
    int id;
    SongRecycleViewAdapter songRecycleViewAdapter;
    MusicAppService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);
        initView();
        Intent i = getIntent();
        name = i.getStringExtra("artist");
        image = i.getStringExtra("image");
        id = Integer.parseInt(i.getStringExtra("id")) ;
        tvNameArtist.setText(name);

        Glide.with(ArtistListActivity.this).load(image).error(R.drawable.anh_dep).into(imgArtist);


        service = ApiClient.getRetrofitInstance().create(MusicAppService.class);
        Call<List<Song>> call = service.findSongByArtistId(id);
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                loadSongList(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Intent i = new Intent(ArtistListActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void initView() {
        imgArtist = findViewById(R.id.imgArtist);
        tvNameArtist = findViewById(R.id.tvNameArtists);
        recyclerViewArtist = findViewById(R.id.recyclerViewArtist);




    }
    private void loadSongList(List< Song > body) {
        songRecycleViewAdapter = new SongRecycleViewAdapter(body,ArtistListActivity.this);
        recyclerViewArtist.setLayoutManager(new GridLayoutManager(ArtistListActivity.this,1));
        recyclerViewArtist.setAdapter(songRecycleViewAdapter);
        songRecycleViewAdapter.setSongList(body);
    }
}