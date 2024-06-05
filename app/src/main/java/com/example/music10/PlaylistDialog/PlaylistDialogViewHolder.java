package com.example.music10.PlaylistDialog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.R;

public class PlaylistDialogViewHolder extends RecyclerView.ViewHolder {
    private TextView playlistName;

    public PlaylistDialogViewHolder(@NonNull View itemView) {
        super(itemView);
        playlistName = itemView.findViewById(R.id.playlist_dialog_name);
    }

    public TextView getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(TextView playlistName) {
        this.playlistName = playlistName;
    }
}
