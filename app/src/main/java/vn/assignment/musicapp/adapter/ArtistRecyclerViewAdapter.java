package vn.assignment.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.assignment.musicapp.R;
import vn.assignment.musicapp.model.Artist;
import vn.assignment.musicapp.model.Song;
import vn.assignment.musicapp.views.activity.ArtistListActivity;
import vn.assignment.musicapp.views.activity.PlayerSongActivity;

public class ArtistRecyclerViewAdapter extends RecyclerView.Adapter{

    private List<Artist> artistList;
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item,parent,false);
        return new ArtistRecyclerViewAdapter.ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArtistRecyclerViewAdapter.ArtistViewHolder artistViewHolder = ( ArtistRecyclerViewAdapter.ArtistViewHolder) holder;
        Artist artist = artistList.get(position);
        artistViewHolder.tvNameArtist.setText(artist.getName());
        Glide.with(context).load(artist.getImage()).error(R.drawable.anh_dep).into(artistViewHolder.imageArtist);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), ArtistListActivity.class);
            i.putExtra("artist",artist.getName());
            i.putExtra("image",artist.getImage());
            i.putExtra("id",artist.getId()+"");


            view.getContext().startActivity(i);
        });
    }
    @Override
    public int getItemCount() {
        {
            return artistList !=null ? artistList.size() : 0;
        }
    }

    public void setArtistList(List<Artist> artists) {
        this.artistList = artistList;
        notifyDataSetChanged();
    }

    public ArtistRecyclerViewAdapter(List<Artist> artistList, Context context) {
        this.context = context;
        this.artistList = artistList;
    }
    class ArtistViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameArtist;
        private ImageView imageArtist;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArtist = itemView.findViewById(R.id.imageArtist);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtistView);

        }

    }
}
