package com.example.music10.Queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.Interfaces.OnDotsClick;
import com.example.music10.Interfaces.OnSongClick;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.R;
import com.example.music10.Songs.SongsViewHolder;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;

public class QueueAdapter extends RecyclerView.Adapter<SongsViewHolder> {
    Context context;
    ArrayList<TrackContainer> queue =new ArrayList<>();
    private OnSongClick queueOnSongClickListener;
    private OnDotsClick queueOnDotsClickListener;


    public QueueAdapter(Context context, ArrayList<TrackContainer> queue, OnSongClick queueOnSongClickListener, OnDotsClick queueOnDotsClickListener) {
        this.context = context;
        this.queue = queue;
        this.queueOnSongClickListener = queueOnSongClickListener;
        this.queueOnDotsClickListener = queueOnDotsClickListener;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final SongsViewHolder queueVh =new SongsViewHolder(LayoutInflater.from(context).inflate(R.layout.track_container, parent, false));
        queueVh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueOnSongClickListener.onSongClick(queue.get(queueVh.getAdapterPosition()), queueVh.getAdapterPosition(), context);
            }
        });
        queueVh.getBtnPopup().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueOnDotsClickListener.onDotsClick(queue.get(queueVh.getAdapterPosition()), queueVh.getAdapterPosition(), context, v);
            }
        });
        return queueVh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        holder.getTrackImg().setImageResource(queue.get(position).getTrack().getImage());
        holder.getNameView().setText(queue.get(position).getTrack().getName());
        holder.getArtistView().setText(queue.get(position).getTrack().getArtist());
        holder.getAlbumView().setText(queue.get(position).getTrack().getAlbum());
        // TODO: 04/05/2024 if not in queue, won't change, change so that dynamic siya
        if (position == MyMediaPlayer.currentIndex){
            holder.getNameView().setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.getNameView().setSelected(true);
        } else {
            holder.getNameView().setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return queue.size();
    }



}
