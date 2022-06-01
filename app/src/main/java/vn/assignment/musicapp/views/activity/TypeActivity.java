package vn.assignment.musicapp.views.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.SongRecycleViewAdapter;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;

public class TypeActivity extends AppCompatActivity {
    ImageView imgType;
    TextView tvNameType;

    RecyclerView recyclerViewType;

    String name,image;
    int id;
    SongRecycleViewAdapter songRecycleViewAdapter;
    MusicAppService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        initView();

        Intent i = getIntent();
        name = i.getStringExtra("type");
        image = i.getStringExtra("image");

        id = Integer.parseInt(i.getStringExtra("id")) ;
        tvNameType.setText(name);

        Glide.with(TypeActivity.this).load(image).error(R.drawable.anh_dep).into(imgType);

        service = ApiClient.getRetrofitInstance().create(MusicAppService.class);
        Call<List<Song>> call = service.findSongByType(id);

        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                loadSongList(response.body());
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
    }

    private void initView() {
        imgType = findViewById(R.id.imgType);
        tvNameType = findViewById(R.id.tvNameType);
        recyclerViewType = findViewById(R.id.recyclerViewType);
    }

    private void loadSongList(List< Song > body) {
        songRecycleViewAdapter = new SongRecycleViewAdapter(body,TypeActivity.this);
        recyclerViewType.setLayoutManager(new GridLayoutManager(TypeActivity.this,1));
        recyclerViewType.setAdapter(songRecycleViewAdapter);
        songRecycleViewAdapter.setSongList(body);
    }
}