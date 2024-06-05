package com.example.music10.Playlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class PlaylistContainer implements Parcelable {
    private Playlist playlist;

    public PlaylistContainer(Playlist playlist) {
        this.playlist = playlist;
    }

    protected PlaylistContainer(Parcel in) {
        playlist = in.readParcelable(Playlist.class.getClassLoader());
    }

    public static final Creator<PlaylistContainer> CREATOR = new Creator<PlaylistContainer>() {
        @Override
        public PlaylistContainer createFromParcel(Parcel in) {
            return new PlaylistContainer(in);
        }

        @Override
        public PlaylistContainer[] newArray(int size) {
            return new PlaylistContainer[size];
        }
    };

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(playlist, flags);
    }
}
