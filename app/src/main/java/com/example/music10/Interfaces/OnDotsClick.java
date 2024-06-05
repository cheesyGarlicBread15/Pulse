package com.example.music10.Interfaces;

import android.content.Context;
import android.view.View;

import com.example.music10.Track.TrackContainer;

public interface OnDotsClick {
    public void onDotsClick(TrackContainer trackContainer, int position, Context context, View v);
}
