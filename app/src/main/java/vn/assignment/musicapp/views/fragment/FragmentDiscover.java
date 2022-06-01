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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.adapter.ArtistRecyclerViewAdapter;
import vn.assignment.musicapp.adapter.SongRecycleViewAdapter;
import vn.assignment.musicapp.adapter.TypesRecyclerAdapter;
import vn.assignment.musicapp.model.Artist;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.model.Type;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;
import vn.assignment.musicapp.views.activity.LoginActivity;

public class FragmentDiscover extends Fragment {


    private SongRecycleViewAdapter songRecycleViewAdapter;
    private ArtistRecyclerViewAdapter artistRecyclerViewAdapter;
    private TypesRecyclerAdapter typesRecyclerAdapter;
    private RecyclerView recyclerView;
    private RecyclerView artistRecyclerView;
    private RecyclerView typeRecyclerView;
    private MusicAppService musicAppService;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewFragmentList);
        artistRecyclerView = view.findViewById(R.id.recyclerViewArtist);
        typeRecyclerView = view.findViewById(R.id.recyclerViewType);
        callApiLoadArtistList();
        callApiLoadSongList();
        callApiLoadTypeList();
    }
    private void callApiLoadTypeList(){
        MusicAppService service = ApiClient.getRetrofitInstance().create(MusicAppService.class);

        Call<List<Type>> call = service.findAllTypes();
        call.enqueue(new Callback<List<Type>>() {
            @Override
            public void onResponse(Call<List<Type>> call, Response<List<Type>> response) {
                loadTypeList(response.body());
            }

            @Override
            public void onFailure(Call<List<Type>> call, Throwable t) {

            }
        });

    }
    private void callApiLoadArtistList() {
        MusicAppService service = ApiClient.getRetrofitInstance().create(MusicAppService.class);

        Call<List<Artist>> call = service.findAllArtist();

        call.enqueue(new Callback<List<Artist>>() {

            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                loadArtistList(response.body());
            }



            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
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

    private void loadSongList(List<Song> body) {

        songRecycleViewAdapter = new SongRecycleViewAdapter(body,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(songRecycleViewAdapter);
    }

    private void loadArtistList(List<Artist> body){
        artistRecyclerViewAdapter = new ArtistRecyclerViewAdapter(body,getContext());
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        artistRecyclerView.setAdapter(artistRecyclerViewAdapter);
    }

    private void loadTypeList(List<Type> body){
        typesRecyclerAdapter = new TypesRecyclerAdapter(body,getContext());
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        typeRecyclerView.setAdapter(typesRecyclerAdapter);
    }
}
