package com.example.music10.Fragments.FragContainer2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music10.MainActivity;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.R;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;


public class MiniPlayerFragment extends Fragment {
    ImageView miniImg;
    TextView miniName, miniArtist, miniAlbum;
    ImageButton miniPrev, miniPausePlay, miniNext;
    ArrayList<TrackContainer> queuedTracks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mini_player, container, false);

        miniImg = view.findViewById(R.id.mini_track_img);
        miniName = view.findViewById(R.id.mini_track_name);
        miniArtist = view.findViewById(R.id.mini_track_artist);
        miniAlbum = view.findViewById(R.id.mini_track_album);
        miniPrev = view.findViewById(R.id.mini_prev);
        miniPausePlay = view.findViewById(R.id.mini_play_pause);
        miniNext = view.findViewById(R.id.mini_next);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (MyMediaPlayer.getInstance() != null){
                    if (MyMediaPlayer.getInstance().isPlaying()){
                        miniPausePlay.setImageResource(R.drawable.pause_fill);
                    } else {
                        miniPausePlay.setImageResource(R.drawable.play_fill);
                    }
                }
                handler.postDelayed(this, 100);
            }
        });

        queuedTracks = new ArrayList<>((MyMediaPlayer.isShuffling) ? QueueFragment.currentShuffledQueue : QueueFragment.currentQueue);

        if (MyMediaPlayer.instance != null){
            updateMiniPlayerControls(queuedTracks);
        }

        return view;
    }

    // TODO: 04/05/2024 if music change in miniplayer, also change the track details
    public void showCurrentPlayingTrack(TrackContainer currentTrack) {
        if (currentTrack != null) {
            miniImg.setImageResource(currentTrack.getTrack().getImage());
            miniName.setText(currentTrack.getTrack().getName());
            miniName.setSelected(true);
            miniName.setTextColor(getResources().getColor(R.color.red));
            miniArtist.setText(currentTrack.getTrack().getArtist());
            miniAlbum.setText(currentTrack.getTrack().getAlbum());
        }
    }

    private void updateMiniPlayerControls(ArrayList<TrackContainer> queue) {
        QueueFragment queueFragment = (QueueFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("QueueFragment");
        if (queueFragment != null) {
            miniPrev.setOnClickListener(v-> queueFragment.playPreviousTrack());
            miniPausePlay.setOnClickListener(v-> queueFragment.pausePlay());
            miniNext.setOnClickListener(v-> queueFragment.playNextTrack());
            showCurrentPlayingTrack(queue.get(0));
        }
    }

}