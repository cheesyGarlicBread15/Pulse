package com.example.music10.Playlist;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.R;

public class PlaylistViewHolder extends RecyclerView.ViewHolder {
    private TextView playlistName;
    private ImageView playlistCover;

    public PlaylistViewHolder(@NonNull View itemView) {
        super(itemView);
        this.playlistName = itemView.findViewById(R.id.playlist_name);
        this.playlistCover = itemView.findViewById(R.id.playlist_cover);
    }

    public TextView getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(TextView playlistName) {
        this.playlistName = playlistName;
    }

    public ImageView getPlaylistCover() {
        return playlistCover;
    }

    public void setPlaylistCover(ImageView playlistCover) {
        this.playlistCover = playlistCover;
    }
}
