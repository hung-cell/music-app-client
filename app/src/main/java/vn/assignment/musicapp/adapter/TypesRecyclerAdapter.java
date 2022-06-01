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
import vn.assignment.musicapp.model.Type;
import vn.assignment.musicapp.views.activity.ArtistListActivity;
import vn.assignment.musicapp.views.activity.TypeActivity;

public class TypesRecyclerAdapter extends RecyclerView.Adapter{
    private List<Type> typeList;
    private Context context;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item,parent,false);
        return new TypesRecyclerAdapter.TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TypesRecyclerAdapter.TypeViewHolder typeViewHolder = ( TypesRecyclerAdapter.TypeViewHolder) holder;
        Type type = typeList.get(position);
        typeViewHolder.tvNameArtist.setText(type.getName());
        Glide.with(context).load(type.getImage()).error(R.drawable.anh_dep).into(typeViewHolder.imageArtist);

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), TypeActivity.class);
            i.putExtra("type",type.getName());
            i.putExtra("image",type.getImage());
            i.putExtra("id",type.getId()+"");


            view.getContext().startActivity(i);
        });
    }
    @Override
    public int getItemCount() {
        {
            return typeList !=null ? typeList.size() : 0;
        }
    }


    public void setArtistList(List<Type> typeList) {
        this.typeList = typeList;
        notifyDataSetChanged();
    }

    public TypesRecyclerAdapter(List<Type> typeList, Context context) {
        this.context = context;
        this.typeList = typeList;
    }
    class TypeViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameArtist;
        private ImageView imageArtist;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArtist = itemView.findViewById(R.id.imageArtist);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtistView);

        }

    }
}
