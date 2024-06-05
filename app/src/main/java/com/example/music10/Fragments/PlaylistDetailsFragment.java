package com.example.music10.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music10.Fragments.BottomDialog.DotsDialogFragment;
import com.example.music10.Interfaces.OnDotsClick;
import com.example.music10.Interfaces.OnSecondContainerChangeFragment;
import com.example.music10.Interfaces.OnSongClick;
import com.example.music10.MainActivity;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.Playlist.Playlist;
import com.example.music10.Playlist.PlaylistContainer;
import com.example.music10.Playlist.Schema.PlaylistDbHelper;
import com.example.music10.Playlist.Schema.PlaylistSongs;
import com.example.music10.R;
import com.example.music10.Songs.SongsAdapter;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;


public class PlaylistDetailsFragment extends Fragment implements OnSongClick, OnDotsClick {
    public static ArrayList<TrackContainer> playlistTracks = new ArrayList<>();
    private TextView plName, numOfTracks;
    ImageView playlistBack;
    PlaylistContainer selectedPlaylist;
    RecyclerView recyclerView;
    PlaylistDbHelper dbHelper;
    private OnSecondContainerChangeFragment listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist_details, container, false);

        //        Register touches only in this fragment, not pass through it
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    return false;
                }
                return true;
            }
        });

        plName = view.findViewById(R.id.playlist_details_name);
        recyclerView = view.findViewById(R.id.playlist_details_recycler_view);
        playlistBack = view.findViewById(R.id.playlist_back);
        selectedPlaylist = ((PlaylistContainer) getArguments().getParcelable("selected_playlist"));
        dbHelper = new PlaylistDbHelper(getContext());

        playlistBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.removeCallbacksAndMessages(null);
                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("PlaylistDetailsFragment");
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_bottom_to_top, R.anim.exit_top_to_bottom)
                        .remove(fragment)
                        .commit();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        MainActivity.fragmentContainerView.setLayoutParams(MainActivity.layoutParams);
                    }
                }, 500);
//                listener.secondContainerChangeFragment(5);
            }
        });

        if (getArguments() != null){
            storeDataInArrays();
            recyclerViewInit();
            setupPlaylist();
        }

        return view;
    }

    private void setupPlaylist(){
        plName.setText(selectedPlaylist.getPlaylist().getPlaylistName());
    }

    private void recyclerViewInit(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SongsAdapter(requireContext(), playlistTracks, this::onSongClick, this::onDotsClick));
    }
    private void storeDataInArrays() {
        playlistTracks.clear();
        Cursor cursor = dbHelper.readPlaylistSongs(dbHelper.getPlaylistIdByName(selectedPlaylist.getPlaylist().getPlaylistName()));
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor != null && cursor.moveToNext()){
                String path = cursor.getString(cursor.getColumnIndexOrThrow(PlaylistSongs.FeedEntry.COLUMN_SONG_PATH));
                TrackContainer track = dbHelper.getTrackByPath(path);
                if (track != null) {
                    playlistTracks.add(track);
                }
            }cursor.close();
        }
    }
    @Override
    public void onDotsClick(TrackContainer trackContainer, int position, Context context, View v) {
        DotsDialogFragment dotsDialogFragment = new DotsDialogFragment().newInstance(trackContainer);
        dotsDialogFragment.show(getParentFragmentManager(), "DotsDialogFragment");
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context != null){
            listener = (OnSecondContainerChangeFragment) context;
        }
    }
    @Override
    public void onSongClick(TrackContainer trackContainer, int position, Context context) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.onPlaylist = true;
        MyMediaPlayer.isTrackClicked = true;
        MyMediaPlayer.isShuffling = false;
        MyMediaPlayer.isLooping = false;
        MyMediaPlayer.originalIndex= position;

        listener.secondContainerChangeFragment(4);
    }
}