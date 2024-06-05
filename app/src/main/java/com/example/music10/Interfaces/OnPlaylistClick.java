package com.example.music10.Interfaces;

import android.content.Context;

import com.example.music10.Playlist.PlaylistContainer;

public interface OnPlaylistClick {
    public void onPlaylistClick(PlaylistContainer playlistContainer, int position, Context context);
}
