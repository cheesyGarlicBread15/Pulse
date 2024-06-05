package com.example.music10.Fragments.FragContainer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.music10.Fragments.PlaylistDetailsFragment;
import com.example.music10.Interfaces.OnPlaylistClick;
import com.example.music10.MainActivity;
import com.example.music10.Playlist.AddPlaylistActivity;
import com.example.music10.Playlist.Playlist;
import com.example.music10.Playlist.PlaylistAdapter;
import com.example.music10.Playlist.PlaylistContainer;
import com.example.music10.Playlist.Schema.PlaylistDbHelper;
import com.example.music10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;


public class PlaylistsFragment extends Fragment implements OnPlaylistClick{
    public static ArrayList<PlaylistContainer> allPlaylists = new ArrayList<>();
    RecyclerView playlistsRecyclerView;
    FloatingActionButton addPlaylist;
    private PlaylistDbHelper dbHelper;
    public static PlaylistAdapter playlistAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);

        dbHelper = new PlaylistDbHelper(getContext());

        playlistsRecyclerView = view.findViewById(R.id.playlist_recyclerview);
        addPlaylist = view.findViewById(R.id.playlist_add_button);
        
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPlaylistActivity.class);
                startActivity(intent);
            }
        });

        storeDataInArrays();


//        Recycler init
        playlistAdapter = new PlaylistAdapter(getContext(), allPlaylists, this::onPlaylistClick);
        playlistsRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
        playlistsRecyclerView.setAdapter(playlistAdapter);


        return view;
    }

    @Override
    public void onPlaylistClick(PlaylistContainer playlistContainer, int position, Context context) {
        Bundle data = new Bundle();
        data.putParcelable("selected_playlist", playlistContainer);
        PlaylistDetailsFragment playlistDetailsFragment = new PlaylistDetailsFragment();
        playlistDetailsFragment.setArguments(data);
        MainActivity.fragmentContainerViewPlaylist.setVisibility(View.VISIBLE);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_for_playlist, playlistDetailsFragment, "PlaylistDetailsFragment")
                .addToBackStack(null)
                .commit();
    }

    private void storeDataInArrays() {
        Cursor cursor = dbHelper.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                allPlaylists.add(new PlaylistContainer(new Playlist(cursor.getString(1))));
            }
        }
    }

    public static void storeNewDatainArrays(){
        Cursor cursor = MainActivity.dbHelper.readNewPlaylist();
        if (cursor.moveToNext()){
            allPlaylists.add(new PlaylistContainer(new Playlist(cursor.getString(1))));
        }
    }


}