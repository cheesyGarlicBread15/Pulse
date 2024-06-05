package com.example.music10.PlaylistDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.Interfaces.OnPlaylistDialogClick;
import com.example.music10.Playlist.PlaylistContainer;
import com.example.music10.R;

import java.util.ArrayList;

public class PlaylistDialogAdapter extends RecyclerView.Adapter<PlaylistDialogViewHolder> {
    Context context;
    ArrayList<PlaylistContainer> allPlaylistDialogContainer = new ArrayList<>();
    private OnPlaylistDialogClick onPlaylistDialogClick;

    public PlaylistDialogAdapter(Context context, ArrayList<PlaylistContainer> allPlaylistDialogContainer, OnPlaylistDialogClick onPlaylistDialogClick) {
        this.context = context;
        this.allPlaylistDialogContainer = allPlaylistDialogContainer;
        this.onPlaylistDialogClick = onPlaylistDialogClick;
    }

    @NonNull
    @Override
    public PlaylistDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final PlaylistDialogViewHolder plVh = new PlaylistDialogViewHolder(LayoutInflater.from(context).inflate(R.layout.playlist_dialog_container, parent, false));
        plVh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaylistDialogClick.onPlaylistDialogClick(allPlaylistDialogContainer.get(plVh.getAdapterPosition()), plVh.getAdapterPosition(), context);
            }
        });

        return plVh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistDialogViewHolder holder, int position) {
        holder.getPlaylistName().setText(allPlaylistDialogContainer.get(position).getPlaylist().getPlaylistName());

    }

    @Override
    public int getItemCount() {
        return allPlaylistDialogContainer.size();
    }
}
