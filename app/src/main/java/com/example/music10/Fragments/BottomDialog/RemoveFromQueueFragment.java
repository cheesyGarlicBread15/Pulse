package com.example.music10.Fragments.BottomDialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music10.Fragments.FragContainer2.QueueFragment;
import com.example.music10.R;
import com.example.music10.Track.TrackContainer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;


public class RemoveFromQueueFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    TextView dotsRemoveTrackName, removeFromQueue;
    TrackContainer removeSelectedTrack;

    public RemoveFromQueueFragment() {

    }

    public static RemoveFromQueueFragment newInstance(TrackContainer trackToRemove){
        RemoveFromQueueFragment fragment = new RemoveFromQueueFragment();
        Bundle args = new Bundle();
        args.putParcelable("remove_track", trackToRemove);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remove_from_queue, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dotsRemoveTrackName = view.findViewById(R.id.btm_remove_dialog_track_name);
        removeFromQueue = view.findViewById(R.id.btm_remove_dialog_remove_queue);

        removeFromQueue.setOnClickListener(this::onClick);

        removeSelectedTrack = ((TrackContainer) getArguments().getParcelable("remove_track"));
        dotsRemoveTrackName.setText(removeSelectedTrack.getTrack().getName());
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btm_remove_dialog_remove_queue){
            QueueFragment fragment = ((QueueFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("QueueFragment"));
            fragment.removeFromQueue(removeSelectedTrack);
            Toast.makeText(getActivity(), "Removed From Queue", Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
    }
}