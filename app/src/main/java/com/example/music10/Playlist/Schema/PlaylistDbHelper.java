package com.example.music10.Playlist.Schema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.music10.R;
import com.example.music10.Track.Track;
import com.example.music10.Track.TrackContainer;

public class PlaylistDbHelper extends SQLiteOpenHelper {
    private Context context;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "playlist.db";
    public static final String FAVORITE_PLAYLIST_NAME = "Favorites";

    public PlaylistDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Playlist.CREATE_ENTRIES);
        db.execSQL(PlaylistSongs.CREATE_ENTRIES);

        // Every time the database is updated or created
        // Then always add a new and first playlist which is the Favorites
        addPlaylist("Favorite");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Playlist.SQL_DELETE_ENTRIES);
        db.execSQL(PlaylistSongs.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * add playlist
     * @param newPlaylist
     * @return createc playlist
     */
    public boolean addPlaylist(String newPlaylist){
        boolean res = true;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // A variable place holder for the values that we want to insert into our table
            ContentValues values = new ContentValues();

            // Add data for each column in the table
            values.put(Playlist.FeedEntry.COLUMN_PLAYLIST_NAME, newPlaylist);

            // Short hand way of inserting data...
            db.insert(Playlist.FeedEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            res = false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return res;
    }


    /**
     * used for getting the track from the path that was stored in the database
     * @param path of the tracks stored in the database
     * @return track of the path
     */
    public TrackContainer getTrackByPath (String path){
        TrackContainer track = null;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = null;
        try {
            String selection = MediaStore.Audio.Media.DATA + " = ?";
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    new String[]{path},
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                track = new TrackContainer(new Track(
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)),
                        path,
                        R.drawable.music));
            }
        } catch (Exception e) {
            Log.e("TrackQuery", "Error retrieving track by path: " + path, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return track;
    }

    /**
     * used to get the id of the selected playlist
     * @param playlistName selected playlist
     * @return ID of the selected playlist retrieved fom the database
     */
    public int getPlaylistIdByName(String playlistName) {
        int playlistId = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            // Query to select the ID of the playlist based on its name
            String query = "SELECT " + Playlist.FeedEntry._ID + " FROM " + Playlist.FeedEntry.TABLE_NAME  + " WHERE " + Playlist.FeedEntry.COLUMN_PLAYLIST_NAME + "= ?";
            cursor = db.rawQuery(query, new String[]{playlistName});
            if (cursor.moveToFirst()) {
                playlistId = cursor.getInt(cursor.getColumnIndexOrThrow(Playlist.FeedEntry._ID));
            }
        } catch (Exception e) {
            Log.e("Database", "Error getting playlist ID for Playlist: " + playlistName, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return playlistId;
    }

    public Cursor readPlaylistSongs(int playlist_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PlaylistSongs.FeedEntry.TABLE_NAME +
                " WHERE " + PlaylistSongs.FeedEntry.COLUMN_PLAYLIST_ID + " = ?";
        Cursor cursor = null;
        if (db != null){cursor = db.rawQuery(query, new String[]{String.valueOf(playlist_id)});}
        return cursor;
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + Playlist.FeedEntry.TABLE_NAME;

        // Here, we call getReadableDatabase() since we only want to fetch data from the database
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){cursor = db.rawQuery(query, null);}
        return cursor;
    }

    /**
     * Used to add song to a chosen playlist
     * @param playlist_id
     * @param track
     * @return if ture then it will store the path
     */
    public boolean addSongToPlaylist(int playlist_id, TrackContainer track){
        boolean res = true;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PlaylistSongs.FeedEntry.COLUMN_PLAYLIST_ID, playlist_id);
            cv.put(PlaylistSongs.FeedEntry.COLUMN_SONG_PATH, track.getTrack().getPath());

            db.insert(PlaylistSongs.FeedEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        }catch (Exception e) { res = false; }
        finally {
            db.endTransaction();
            db.close();
        }
        return res;
    }

    /**
     * if the favorite playlist doesn't exist, it creates it
     *
     */
    public void createFavoritePlaylistIfNotExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + Playlist.FeedEntry.TABLE_NAME +
                " WHERE " + Playlist.FeedEntry.COLUMN_PLAYLIST_NAME + " = ?";


        Cursor cursor = db.rawQuery(query, new String[]{FAVORITE_PLAYLIST_NAME});

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(Playlist.FeedEntry.COLUMN_PLAYLIST_NAME, FAVORITE_PLAYLIST_NAME);
            db.insert(Playlist.FeedEntry.TABLE_NAME, null, values);
        }

        cursor.close();
    }

    /**
     * it reads new playlist created
     * @return cursor object that holds the playlist
     */
    public Cursor readNewPlaylist() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Playlist.FeedEntry.TABLE_NAME +
                " ORDER BY " + Playlist.FeedEntry._ID + " DESC LIMIT 1";
        return (Cursor) db.rawQuery(query, null);
    }




}
