package vn.assignment.musicapp.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerViewSearch;
    SongRecycleViewAdapter songRecycleViewAdapter;
    List<Song> listSong;
    MusicAppService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        listSong = new ArrayList<>();
        songRecycleViewAdapter = new SongRecycleViewAdapter(listSong,SearchActivity.this);

        recyclerViewSearch.setLayoutManager(new GridLayoutManager(SearchActivity.this,1));
        recyclerViewSearch.setAdapter(songRecycleViewAdapter);
        service = ApiClient.getRetrofitInstance().create(MusicAppService.class);









        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callApiSearchSong(newText);
                return false;
            }
        });


    }

    private void callApiSearchSong(String newText) {
        Call<List<Song>> call = service.seachSong(newText);

        call.enqueue(new Callback<List<Song>>() {

            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                loadSongList(response.body());
            }



            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Intent i = new Intent(SearchActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadSongList(List< Song > body) {

        songRecycleViewAdapter.setSongList(body);
    }
}