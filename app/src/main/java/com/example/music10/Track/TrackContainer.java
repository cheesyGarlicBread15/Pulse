package com.example.music10.Track;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class TrackContainer implements Serializable, Parcelable {
    private Track track;

    public TrackContainer(Track track) {
        this.track = track;
    }

    protected TrackContainer(Parcel in) {
    }

    public static final Creator<TrackContainer> CREATOR = new Creator<TrackContainer>() {
        @Override
        public TrackContainer createFromParcel(Parcel in) {
            return new TrackContainer(in);
        }

        @Override
        public TrackContainer[] newArray(int size) {
            return new TrackContainer[size];
        }
    };

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
