package com.example.music10.Playlist.Schema;

import android.provider.BaseColumns;

public class Playlist {
    public Playlist(){}

    public static class FeedEntry implements BaseColumns{
        // Define table and column names
        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_PLAYLIST_NAME = "playlist_container";
    }

    public static final String CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME
            + " (" + FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FeedEntry.COLUMN_PLAYLIST_NAME + " TEXT NOT NULL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
