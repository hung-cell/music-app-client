package vn.assignment.musicapp.adapter;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.mediaplayer.MyMediaPlayer;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;
import vn.assignment.musicapp.views.activity.LocalSongActivty;
import vn.assignment.musicapp.views.activity.PlayerSongActivity;

public class SongRecycleViewAdapter extends RecyclerView.Adapter{
    private List<Song> songList;
    private Context context;
    private String prevActivity;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SongViewHolder songViewHolder = (SongViewHolder) holder;
        Song song = songList.get(position);
        songViewHolder.tvNameSong.setText(song.getSong());
        songViewHolder.tvNameArtist.setText(song.getArtist());
        songViewHolder.dropdown.setVisibility(View.INVISIBLE);

        Glide.with(context).load(song.getImage()).error(R.drawable.anh_dep).into(songViewHolder.imageSong);
        if (prevActivity != "LOCAL"){
            holder.itemView.setOnClickListener(view -> {
              playMusic(position,view,song);
            });
        }

        if (prevActivity == "LOCAL"){
            songViewHolder.dropdown.setVisibility(View.VISIBLE);
            songViewHolder.dropdown.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, songViewHolder.dropdown);
                popupMenu.inflate(R.menu.option_item_delete);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {


                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete:
                                File directory = new File(song.getUrl());
                                directory.delete();
                                songList.remove(song);
                                notifyDataSetChanged();
                        }
                        return false;
                    }
                });
                popupMenu.show();

            });
            songViewHolder.imageSong.setOnClickListener(view -> {
                playMusic(position,view,song);
            });
            songViewHolder.areaDetail.setOnClickListener(view -> {
                playMusic(position,view,song);
            });
        }
    }

    private void playMusic(int position, View view, Song song) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.currentSong = position;
        Intent i = new Intent(view.getContext(), PlayerSongActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("listSong",(Serializable) songList);
        i.putExtra("bundle",args);

        MusicAppService musicAppService = ApiClient.getRetrofitInstance().create(MusicAppService.class);
        Call<Void> call = musicAppService.increseView(song.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                view.getContext().startActivity(i);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        {
            return songList !=null ? songList.size() : 0;
        }
    }

    public interface ItemClickListener{
        void onItemClick(Song song);
    }
    private ItemClickListener itemClickListener;

    public void setSongList(List<Song> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }
    public void setPrevActivity(String activity) {
        this.prevActivity = activity;
        notifyDataSetChanged();
    }
    public SongRecycleViewAdapter(List<Song> songList, Context context) {
        this.context = context;
        this.songList = songList;

    }
    class SongViewHolder extends RecyclerView.ViewHolder{
        LinearLayout areaDetail;
        private TextView tvNameSong, tvNameArtist;
        private ImageView imageSong;
        private TextView dropdown;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            areaDetail = itemView.findViewById(R.id.area_detail);
            dropdown = itemView.findViewById(R.id.textViewOptions);
            imageSong = itemView.findViewById(R.id.imageSong);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist);
            tvNameSong = itemView.findViewById(R.id.tvNameSong);
        }

    }
}
