package com.example.music10.Interfaces;

import android.content.Context;

import com.example.music10.Track.TrackContainer;

public interface OnSongClick {
    public void onSongClick(TrackContainer trackContainer, int position, Context context);
}
