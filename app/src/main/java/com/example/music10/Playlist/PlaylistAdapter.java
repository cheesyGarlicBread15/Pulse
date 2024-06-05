package com.example.music10.Playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.Interfaces.OnPlaylistClick;
import com.example.music10.R;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistViewHolder> {
    Context context;
    ArrayList<PlaylistContainer> allPlaylistsContainer = new ArrayList<>();
    private OnPlaylistClick onPlaylistClickListener;

    public PlaylistAdapter(Context context, ArrayList<PlaylistContainer> allPlaylistsContainer, OnPlaylistClick onPlaylistClickListener) {
        this.context = context;
        this.allPlaylistsContainer = allPlaylistsContainer;
        this.onPlaylistClickListener = onPlaylistClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final PlaylistViewHolder plVh = new PlaylistViewHolder(LayoutInflater.from(context).inflate(R.layout.playlist_container, parent, false));
        plVh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaylistClickListener.onPlaylistClick(allPlaylistsContainer.get(plVh.getAdapterPosition()), plVh.getAdapterPosition(), context);
            }
        });
        return plVh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        holder.getPlaylistName().setText(allPlaylistsContainer.get(position).getPlaylist().getPlaylistName());
//        holder.getPlaylistCover().setImageResource(allPlaylistsContainer.get(position).getPlaylist().getPlaylistCover());
    }

    @Override
    public int getItemCount() {
        return allPlaylistsContainer.size();
    }
}
