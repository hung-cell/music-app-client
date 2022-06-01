package vn.assignment.musicapp.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.SongRecycleViewAdapter;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;
import vn.assignment.musicapp.views.activity.LoginActivity;

public class FragmentChart extends Fragment {
    private RecyclerView recyclerView;
    private SongRecycleViewAdapter songRecycleViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewTop);

        callApiLoadSongList();
    }

    private void callApiLoadSongList() {
        MusicAppService service = ApiClient.getRetrofitInstance().create(MusicAppService.class);

        Call<List<Song>> call = service.findAllSong();

        call.enqueue(new Callback<List<Song>>() {

            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                loadSongList(response.body());
            }



            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void sortByView(List<Song> list){
        Collections.sort(list, new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                if(song1.getView() < song2.getView()) return  1;
                else return -1;
            }
        });
    }
    private void loadSongList(List<Song> body) {

        sortByView(body);
        List<Song> listTop = new ArrayList<>();
        if(body.size() <10) listTop = body;
        else{
            for(int i=0;i<10;i++){
                listTop.add(body.get(i));
            }
        }
        songRecycleViewAdapter = new SongRecycleViewAdapter(listTop,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(songRecycleViewAdapter);
    }
}
