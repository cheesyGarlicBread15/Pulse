package com.example.music10.Fragments.FragContainer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music10.Fragments.BottomDialog.DotsDialogFragment;
import com.example.music10.Interfaces.OnDotsClick;
import com.example.music10.Interfaces.OnSecondContainerChangeFragment;
import com.example.music10.Interfaces.OnSongClick;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.R;
import com.example.music10.Utils.Sort;
import com.example.music10.Songs.SongsAdapter;
import com.example.music10.Track.Track;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;


public class SongsFragment extends Fragment implements OnSongClick, OnDotsClick{
    public static ArrayList<TrackContainer> allTracks =new ArrayList<>();
    RecyclerView allSongsRecyclerView;
    private OnSecondContainerChangeFragment listener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initSongs();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        allSongsRecyclerView = view.findViewById(R.id.all_songs_recycler_view);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewInit();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context != null){
            listener = (OnSecondContainerChangeFragment) context;
        }
    }

    private void recyclerViewInit(){
        Sort.sortObjects(allTracks, new Track.NameComparator());
        allSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allSongsRecyclerView.setAdapter(new SongsAdapter(requireContext(), allTracks, this::onSongClick, this::onDotsClick));
    }

    @Override
    public void onSongClick(TrackContainer trackContainer, int position, Context context) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.onAllSongs = true;
        MyMediaPlayer.isTrackClicked = true;
        MyMediaPlayer.isShuffling = false;
        MyMediaPlayer.isLooping = false;
        MyMediaPlayer.originalIndex= position;

        listener.secondContainerChangeFragment(4);
    }

    @Override
    public void onDotsClick(TrackContainer trackContainer, int position, Context context, View v) {
        DotsDialogFragment dotsDialogFragment = new DotsDialogFragment().newInstance(trackContainer);
        dotsDialogFragment.show(getParentFragmentManager(), "DotsDialogFragment");
    }

}