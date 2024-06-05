package com.example.music10.Playlist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.music10.Fragments.FragContainer.PlaylistsFragment;
import com.example.music10.Playlist.Schema.PlaylistDbHelper;
import com.example.music10.R;

import java.util.ArrayList;

public class AddPlaylistActivity extends AppCompatActivity {
    EditText playlistTitle;
    Button addPlaylistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playlistTitle = findViewById(R.id.playlist_name_add);
        addPlaylistBtn = findViewById(R.id.add_playlist);
        PlaylistDbHelper db = new PlaylistDbHelper(getApplicationContext());

        addPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlistTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(AddPlaylistActivity.this, "Enter Playlist Name", Toast.LENGTH_SHORT).show();
                } else if (playlistNameExists(playlistTitle.getText().toString().trim())) {
                    Toast.makeText(AddPlaylistActivity.this, "The Playlist Already Existed", Toast.LENGTH_SHORT).show();
                } else {
                    db.addPlaylist(playlistTitle.getText().toString().trim());
                    PlaylistsFragment.storeNewDatainArrays();
                    PlaylistsFragment.playlistAdapter.notifyDataSetChanged();
                    finish();
                }
            }
        });

    }

    private boolean playlistNameExists(String plNameToAdd){
        for (PlaylistContainer pl :
                PlaylistsFragment.allPlaylists) {
            if (pl.getPlaylist().getPlaylistName().equals(plNameToAdd)){
                return true;
            }
        }
        return false;
    }
}