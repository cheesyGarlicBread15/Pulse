package com.example.music10.Songs;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music10.R;

public class SongsViewHolder extends RecyclerView.ViewHolder {
    private ImageView trackImg;
    private TextView nameView, artistView, albumView;
    private ImageButton btnPopup;

    public SongsViewHolder(@NonNull View itemView) {
        super(itemView);
        trackImg = itemView.findViewById(R.id.track_img);
        nameView = itemView.findViewById(R.id.track_name);
        artistView = itemView.findViewById(R.id.track_artist);
        albumView = itemView.findViewById(R.id.track_album);
        btnPopup = itemView.findViewById(R.id.songs_fragment_popup);
    }

    public ImageView getTrackImg() {
        return trackImg;
    }

    public void setTrackImg(ImageView trackImg) {
        this.trackImg = trackImg;
    }

    public TextView getNameView() {
        return nameView;
    }

    public void setNameView(TextView nameView) {
        this.nameView = nameView;
    }

    public TextView getArtistView() {
        return artistView;
    }

    public void setArtistView(TextView artistView) {
        this.artistView = artistView;
    }

    public TextView getAlbumView() {
        return albumView;
    }

    public void setAlbumView(TextView albumView) {
        this.albumView = albumView;
    }

    public ImageButton getBtnPopup() {
        return btnPopup;
    }

    public void setBtnPopup(ImageButton btnPopup) {
        this.btnPopup = btnPopup;
    }
}
