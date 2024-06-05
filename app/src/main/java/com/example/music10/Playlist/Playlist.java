package com.example.music10.Playlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;

public class Playlist implements Parcelable {
    String playlistName;
    ArrayList<TrackContainer> playlistTracks;
    int playlistCover;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        this.playlistTracks = playlistTracks;
        this.playlistCover = playlistCover;
    }

    protected Playlist(Parcel in) {
        playlistName = in.readString();
        playlistCover = in.readInt();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public ArrayList<TrackContainer> getPlaylistTracks() {
        return playlistTracks;
    }

    public void setPlaylistTracks(ArrayList<TrackContainer> playlistTracks) {
        this.playlistTracks = playlistTracks;
    }

    public int getPlaylistCover() {
        return playlistCover;
    }

    public void setPlaylistCover(int playlistCover) {
        this.playlistCover = playlistCover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(playlistName);
        dest.writeInt(playlistCover);
    }
}
