package com.example.music10.Songs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.Interfaces.OnDotsClick;
import com.example.music10.Interfaces.OnSongClick;
import com.example.music10.R;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {
    Context context;
    ArrayList<TrackContainer> allTracksContainer = new ArrayList<>();
    private OnSongClick onSongClickListener;
    private OnDotsClick onDotsClickListener;


    public SongsAdapter(Context context, ArrayList<TrackContainer> allTracksContainer, OnSongClick onSongClickListener, OnDotsClick onDotsClickListener) {
        this.context = context;
        this.allTracksContainer = allTracksContainer;
        this.onSongClickListener = onSongClickListener;
        this.onDotsClickListener = onDotsClickListener;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final SongsViewHolder vh = new SongsViewHolder(LayoutInflater.from(context).inflate(R.layout.track_container, parent, false));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongClickListener.onSongClick(allTracksContainer.get(vh.getAdapterPosition()), vh.getAdapterPosition(), context);

            }
        });
        vh.getBtnPopup().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDotsClickListener.onDotsClick(allTracksContainer.get(vh.getAdapterPosition()), vh.getAdapterPosition(), context, v);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        holder.getTrackImg().setImageResource(allTracksContainer.get(position).getTrack().getImage());
        holder.getNameView().setText(allTracksContainer.get(position).getTrack().getName());
        holder.getArtistView().setText(allTracksContainer.get(position).getTrack().getArtist());
        holder.getAlbumView().setText(allTracksContainer.get(position).getTrack().getAlbum());
    }

    @Override
    public int getItemCount() {
        return allTracksContainer.size();
    }
}
