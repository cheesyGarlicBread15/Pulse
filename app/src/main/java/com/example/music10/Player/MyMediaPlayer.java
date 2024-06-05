package com.example.music10.Player;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    public static MediaPlayer instance;
    public static int currentIndex = 0, originalIndex, clickedIndex, shuffledIndex;
    public static boolean
            isLooping = false,
            isShuffling = false,
            isTrackClicked = false,
            isPrevNextClicked = false,
            isTrackClicked2 = false,
            onComplete = false,
            completionListenerSet = true,
            isFavorite = false,
            onAllSongs = false,
            onPlaylist = false;

    public static MediaPlayer getInstance(){
        if (instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }
}
