package com.example.music10.Fragments.BottomDialog;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music10.Fragments.FragContainer.PlaylistsFragment;
import com.example.music10.Interfaces.OnPlaylistDialogClick;
import com.example.music10.MainActivity;
import com.example.music10.Playlist.Playlist;
import com.example.music10.Playlist.PlaylistContainer;
import com.example.music10.Playlist.Schema.PlaylistDbHelper;
import com.example.music10.PlaylistDialog.PlaylistDialogAdapter;
import com.example.music10.R;
import com.example.music10.Track.TrackContainer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class PlaylistDialogFragment extends BottomSheetDialogFragment implements OnPlaylistDialogClick{
    private ArrayList<PlaylistContainer> allPlaylistsDialog = new ArrayList<>();
    TrackContainer currentSelectedTrack;
    TextView empty;
    RecyclerView allPlaylistsDialogRecyclerView;
    private OnPlaylistDialogClick onPlaylistDialogClick;

    public static PlaylistDialogFragment newInstance(TrackContainer trackToAdd){
        PlaylistDialogFragment fragment =new PlaylistDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("track", trackToAdd);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist_dialog, container, false);

        empty = view.findViewById(R.id.no_playlists);
        allPlaylistsDialogRecyclerView = view.findViewById(R.id.playlist_dialog_recycler_view);

        currentSelectedTrack = (TrackContainer) getArguments().getParcelable("track");

        storeDataInArrays();
        if (allPlaylistsDialog.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }
        recyclerViewInit();

        return view;
    }

    private void recyclerViewInit(){
        allPlaylistsDialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allPlaylistsDialogRecyclerView.setAdapter(new PlaylistDialogAdapter(requireContext(), allPlaylistsDialog, this::onPlaylistDialogClick));
    }


    @Override
    public void onPlaylistDialogClick(PlaylistContainer playlistContainer, int position, Context context) {
        PlaylistDbHelper db = new PlaylistDbHelper(getContext());
        db.addSongToPlaylist(db.getPlaylistIdByName(allPlaylistsDialog.get(position).getPlaylist().getPlaylistName()), currentSelectedTrack);
        Toast.makeText(getContext(), "Added to: " + allPlaylistsDialog.get(position).getPlaylist().getPlaylistName(), Toast.LENGTH_LONG).show();
    }

    public void storeDataInArrays(){
        Cursor cursor = MainActivity.dbHelper.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                allPlaylistsDialog.add(new PlaylistContainer(new Playlist(cursor.getString(1))));
            }
        }
    }
}