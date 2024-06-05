package com.example.music10.Interfaces;

public interface MediaControls {
    public void setupPlayer();

    public void playNextTrack();

    public void playPreviousTrack();

    public void pausePlay();

    public void shuffleQueue();

    public void swapTracks(int i, int j);

    public void loopQueue();

    public void playTrack();

    public String convertToTimeFormat(String duration);

    public void setupControls();

}
