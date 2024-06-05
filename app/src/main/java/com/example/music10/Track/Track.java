package com.example.music10.Track;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Comparator;

public class Track implements Parcelable {
    private String name, artist, album, duration, path;
    private int image;
    private boolean favorite;

    public Track(String name, String artist, String album, String duration, String path, int image) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.image = image;
    }

    protected Track(Parcel in) {
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        path = in.readString();
        image = in.readInt();
        favorite = in.readByte() != 0;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeString(path);
        dest.writeInt(image);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    //    Inner comparator class for sorting
    public static class NameComparator implements Comparator<Track>{
        @Override
        public int compare(Track o1, Track o2) {
            return o1.getName().trim().toLowerCase().compareTo(o2.getName().trim().toLowerCase());
        }
    }
}
