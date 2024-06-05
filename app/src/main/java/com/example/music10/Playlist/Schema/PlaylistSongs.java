package com.example.music10.Playlist.Schema;

import android.provider.BaseColumns;

public class PlaylistSongs {

    public PlaylistSongs(){}

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "playlist_songs";
        public static final String COLUMN_PLAYLIST_ID = "playlist_id";
        public static final String COLUMN_SONG_PATH = "song_path";
    }

    public static final String CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME + " (" +
            FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FeedEntry.COLUMN_PLAYLIST_ID + " INTEGER, " +
            FeedEntry.COLUMN_SONG_PATH + " TEXT NOT NULL, " +
            " FOREIGN KEY (" + FeedEntry.COLUMN_PLAYLIST_ID + ") REFERENCES " +
            Playlist.FeedEntry.TABLE_NAME + "(" +Playlist.FeedEntry._ID + "))";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


}
