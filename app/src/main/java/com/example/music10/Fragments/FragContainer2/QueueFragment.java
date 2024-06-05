package com.example.music10.Fragments.FragContainer2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.music10.Fragments.BottomDialog.DotsDialogFragment;
import com.example.music10.Fragments.BottomDialog.RemoveFromQueueFragment;
import com.example.music10.Interfaces.MediaControls;
import com.example.music10.Interfaces.MiniPlayerFragmentListener;
import com.example.music10.Interfaces.OnDotsClick;
import com.example.music10.Interfaces.OnSecondContainerChangeFragment;
import com.example.music10.Interfaces.OnSongClick;
import com.example.music10.Interfaces.QueueFragmentListener;
import com.example.music10.MainActivity;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.Queue.QueueAdapter;
import com.example.music10.R;
import com.example.music10.Track.TrackContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class QueueFragment extends Fragment implements OnSongClick, OnDotsClick, MediaControls {
    TextView queueCurrentTime, queueTotalTime;
    SeekBar queueSeekbar;
    ImageView queuePausePlay, queueNextTrack, queuePrevTrack, queueShuffleQueue, queueLoopQueue, queueBack;
    public static RecyclerView queueRecyclerView;
    public static TrackContainer currentTrack;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    public static QueueAdapter queueAdapter;
    LinearLayoutManager layoutManager;
    private OnSecondContainerChangeFragment listener;
    public static ArrayList<TrackContainer> originalQueue = new ArrayList<>(), shuffledQueue = new ArrayList<>(),
            currentQueue = new ArrayList<>(), currentShuffledQueue = new ArrayList<>();
    private QueueFragmentListener queueFragmentListener;
    private MiniPlayerFragmentListener miniPlayerFragmentListener;
    Handler seekBarHandler = new Handler(Looper.getMainLooper());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue, container, false);
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
        queueCurrentTime = view.findViewById(R.id.queue_current_time);
        queueTotalTime = view.findViewById(R.id.queue_total_time);
        queueSeekbar = view.findViewById(R.id.queue_seek_bar);
        queuePausePlay = view.findViewById(R.id.queue_play_pause);
        queueNextTrack = view.findViewById(R.id.queue_next);
        queuePrevTrack = view.findViewById(R.id.queue_previous);
        queueShuffleQueue = view.findViewById(R.id.queue_shuffle);
        queueLoopQueue = view.findViewById(R.id.queue_loop);
        queueBack = view.findViewById(R.id.queue_back);
        queueRecyclerView = view.findViewById(R.id.queue_recycler_view);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MyMediaPlayer.onComplete = true;
                playNextTrack();
            }

        });

        queueBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.removeCallbacksAndMessages(null);
                Fragment fragment = requireActivity().getSupportFragmentManager().findFragmentByTag("QueueFragment");
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
                listener.secondContainerChangeFragment(5);
            }
        });


        seekBarHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    queueSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                    queueCurrentTime.setText(convertToTimeFormat(String.valueOf(mediaPlayer.getCurrentPosition())));
                    if (mediaPlayer.isPlaying()){
                        queuePausePlay.setImageResource(R.drawable.pause_fill);
                    } else {
                        queuePausePlay.setImageResource(R.drawable.play_fill);
                    }
                }
                seekBarHandler.postDelayed(this, 100);
            }
        });

        queueSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        queueFragmentListener.onQueueFragmentReady();

        setupButtonRes();
        setupPlayer();

        return view;
    }

    public static ArrayList<TrackContainer> getOriginalQueue() {
        return originalQueue;
    }

    public static void setOriginalQueue(ArrayList<TrackContainer> originalQueue) {
        QueueFragment.originalQueue = new ArrayList<>(originalQueue);
    }

    public static ArrayList<TrackContainer> getShuffledQueue() {
        return shuffledQueue;
    }

    public static void setShuffledQueue(ArrayList<TrackContainer> shuffledQueue) {
        QueueFragment.shuffledQueue = new ArrayList<>(shuffledQueue);
    }

    public static void addToQueue(TrackContainer track){
        originalQueue.add(track);
        currentQueue.add(track);
        if (!shuffledQueue.isEmpty()){
            shuffledQueue.add(track);
            currentShuffledQueue.add(track);
        }
        queueAdapter.notifyDataSetChanged();
    }

    public void removeFromQueue(TrackContainer track){
        originalQueue.remove(getTrackOriginalPosition(track, originalQueue));
        if (!shuffledQueue.isEmpty()){
            shuffledQueue.remove(getTrackOriginalPosition(track, shuffledQueue));
        }
        MyMediaPlayer.isPrevNextClicked = true;
        if (getTrackOriginalPosition(track, currentQueue) == 0){
            playNextTrack();
            return;
        } else if (!currentShuffledQueue.isEmpty() && getTrackOriginalPosition(track, currentShuffledQueue) == 0) {
            playNextTrack();
            return;
        }
        currentQueue.remove(getTrackOriginalPosition(track, currentQueue));
        if (!currentShuffledQueue.isEmpty()){
            currentShuffledQueue.remove(getTrackOriginalPosition(track, currentShuffledQueue));
        }
        recyclerViewInit((MyMediaPlayer.isShuffling) ? currentShuffledQueue  : currentQueue);

        queueAdapter.notifyDataSetChanged();
    }

    private void recyclerViewInit(ArrayList<TrackContainer> queue){
        if (MyMediaPlayer.isPrevNextClicked && !queue.isEmpty()){
            MyMediaPlayer.isPrevNextClicked = false;
            queueAdapter.notifyDataSetChanged();
        } else {
            layoutManager = new LinearLayoutManager(getContext());
            queueRecyclerView.setLayoutManager(layoutManager);
            queueAdapter = new QueueAdapter(requireContext(), queue, this::onSongClick, this::onDotsClick);
            queueRecyclerView.setAdapter(queueAdapter);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSecondContainerChangeFragment) {
            listener = (OnSecondContainerChangeFragment) context;
        }
        if (context instanceof QueueFragmentListener) {
            queueFragmentListener = (QueueFragmentListener) context;
        }
        if (context instanceof MiniPlayerFragmentListener){
            miniPlayerFragmentListener = (MiniPlayerFragmentListener) context;
        }
    }

    @Override
    public void onSongClick(TrackContainer trackContainer, int position, Context context) {
        MyMediaPlayer.getInstance().reset();
        MyMediaPlayer.clickedIndex = position;
        MyMediaPlayer.isTrackClicked2 = true;
        if (!MyMediaPlayer.isShuffling){
            MyMediaPlayer.originalIndex = getTrackOriginalPosition(currentQueue.get(MyMediaPlayer.clickedIndex), originalQueue);
        } else {
            MyMediaPlayer.shuffledIndex = getTrackOriginalPosition(currentShuffledQueue.get(MyMediaPlayer.clickedIndex), shuffledQueue);
        }

        setupPlayer();
    }

    @Override
    public void onDotsClick(TrackContainer trackContainer, int position, Context context, View v) {
        RemoveFromQueueFragment removeFromQueueFragment = new RemoveFromQueueFragment().newInstance(trackContainer);
        removeFromQueueFragment.show(getParentFragmentManager(), "RemoveFromQueueFragment");
//        DotsDialogFragment dotsDialogFragment = new DotsDialogFragment().newInstance(trackContainer);
//        dotsDialogFragment.show(getParentFragmentManager(), "DotsDialogFragment");
    }

    @Override
    public void playTrack() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentTrack.getTrack().getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            queueSeekbar.setProgress(0);
            queueSeekbar.setMax(mediaPlayer.getDuration());
            if (((MiniPlayerFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("MiniPlayerFragment")) != null){
                miniPlayerFragmentListener.onMiniPlayerFragmentReady();
            }
            queueAdapter.notifyDataSetChanged();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setupPlayer() {
        //        If clicked from All Songs or Playlists
        if (MyMediaPlayer.isTrackClicked){
            currentTrack = originalQueue.get(MyMediaPlayer.originalIndex);
            currentQueue = new ArrayList<>(originalQueue.subList(MyMediaPlayer.originalIndex, originalQueue.size()));
            MyMediaPlayer.isTrackClicked = false;
        } else if (MyMediaPlayer.isTrackClicked2) {
//            If clicked from queue and shuffling
            if (MyMediaPlayer.isShuffling){
                currentTrack = currentShuffledQueue.get(MyMediaPlayer.clickedIndex);
                currentShuffledQueue = new ArrayList<>(currentShuffledQueue.subList(MyMediaPlayer.clickedIndex, currentShuffledQueue.size()));
            } else {
//                If clicked from queue and not shuffling
                currentTrack = currentQueue.get(MyMediaPlayer.clickedIndex);
                currentQueue = new ArrayList<>(currentQueue.subList(MyMediaPlayer.clickedIndex, currentQueue.size()));
            }
            MyMediaPlayer.isTrackClicked2 = false;
        } else {
//            Next, shuffle onclick, onComp (no loop)
            if (MyMediaPlayer.isShuffling){
                currentTrack = currentShuffledQueue.get(0);
            } else {
                currentTrack = currentQueue.get(0);
            }

        }
        if (!MyMediaPlayer.isShuffling){
            recyclerViewInit(currentQueue);
        } else {
            recyclerViewInit(currentShuffledQueue);
        }

        setupControls();
        if (!mediaPlayer.isPlaying()) {
            playTrack();
        }
    }

    @Override
    public void setupControls() {
//        if last song and when onCompletion or playNextTrack
        if (MyMediaPlayer.onComplete){
            try {
                MyMediaPlayer.onComplete = false;
                mediaPlayer.setDataSource(currentTrack.getTrack().getPath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        queueSeekbar.setProgress(0);
        queueSeekbar.setMax(mediaPlayer.getDuration());
        queueTotalTime.setText(convertToTimeFormat(currentTrack.getTrack().getDuration()));
        queuePausePlay.setOnClickListener(v-> pausePlay());
        queueNextTrack.setOnClickListener(v-> playNextTrack());
        queuePrevTrack.setOnClickListener(v-> playPreviousTrack());
        queueShuffleQueue.setOnClickListener(v-> shuffleQueue());
        queueLoopQueue.setOnClickListener(v-> loopQueue());
        queueAdapter.notifyDataSetChanged();
    }

    @Override
    public void playNextTrack() {
        MyMediaPlayer.isPrevNextClicked = true;
        if (!MyMediaPlayer.isShuffling){
            MyMediaPlayer.originalIndex += 1;
            currentQueue.remove(0);
            if (MyMediaPlayer.originalIndex == originalQueue.size()){
                MyMediaPlayer.originalIndex = 0;
                currentQueue.clear();
                currentQueue.addAll(originalQueue);
                currentTrack = currentQueue.get(0);
                
                if (!MyMediaPlayer.isLooping){
                    MyMediaPlayer.completionListenerSet = false;
                    mediaPlayer.setOnCompletionListener(null);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.seekTo(0);
                    setupControls();
                    recyclerViewInit(currentQueue);

//                    Delay the new completion listener so that it wont execute together with the removing of the first
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!MyMediaPlayer.completionListenerSet){
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        MyMediaPlayer.onComplete = true;
                                        playNextTrack();
                                    }
                                });
                            }

                        }
                    }, 1);
                    return;
                }
//                looping handled in setupplayer
            }

        } else {
            MyMediaPlayer.shuffledIndex += 1;
            currentShuffledQueue.remove(0);
            if (MyMediaPlayer.shuffledIndex == shuffledQueue.size()){
                MyMediaPlayer.shuffledIndex = 0;
                currentShuffledQueue.clear();
                currentShuffledQueue.addAll(shuffledQueue);
                currentTrack = currentShuffledQueue.get(0);
                if (!MyMediaPlayer.isLooping){
                    MyMediaPlayer.completionListenerSet = false;
                    mediaPlayer.setOnCompletionListener(null);
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.seekTo(0);
                    setupControls();
                    recyclerViewInit(currentShuffledQueue);

//                    Delay the new completion listener so that it wont execute together with the removing of the first
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!MyMediaPlayer.completionListenerSet){
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        MyMediaPlayer.onComplete = true;
                                        playNextTrack();
                                    }
                                });
                            }

                        }
                    }, 1);
                    return;
                }
            }
        }

        mediaPlayer.reset();
        setupPlayer();
    }

    @Override
    public void playPreviousTrack() {
        if (!MyMediaPlayer.isShuffling){
            MyMediaPlayer.originalIndex -= 1;
            if (MyMediaPlayer.originalIndex == -1){
                if (MyMediaPlayer.isLooping){
                    MyMediaPlayer.originalIndex = originalQueue.size() - 1;
                    currentQueue.add(0, originalQueue.get(MyMediaPlayer.originalIndex));
                    currentQueue.subList(1, currentQueue.size()).clear();
                } else {
                    MyMediaPlayer.originalIndex = 0;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.seekTo(0);
                    setupControls();
                }
            } else {
                currentQueue.add(0, originalQueue.get(MyMediaPlayer.originalIndex));
            }
        } else {
            MyMediaPlayer.shuffledIndex -= 1;
            if (MyMediaPlayer.shuffledIndex == -1){
                if (MyMediaPlayer.isLooping){
                    MyMediaPlayer.shuffledIndex = shuffledQueue.size() - 1;
                    currentShuffledQueue.add(0, shuffledQueue.get(MyMediaPlayer.shuffledIndex));
                    currentShuffledQueue.subList(1, currentShuffledQueue.size()).clear();
                } else {
                    MyMediaPlayer.shuffledIndex = 0;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.seekTo(0);
                    setupControls();
                }
            } else {
                currentShuffledQueue.add(0, shuffledQueue.get(MyMediaPlayer.shuffledIndex));
            }
        }

        mediaPlayer.reset();
        setupPlayer();
    }

    @Override
    public void pausePlay() {
        // TODO: 08/05/2024 when pausing in miniplayer and click queue, it will restart music 
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    @Override
    public void shuffleQueue() {
        MyMediaPlayer.isShuffling = !MyMediaPlayer.isShuffling;
        if (MyMediaPlayer.isShuffling){
            queueShuffleQueue.setImageResource(R.drawable.shuffle_fill);
            shuffledQueue.clear();
            shuffledQueue = new ArrayList<>(originalQueue);
            swapTracks(0, getTrackOriginalPosition(currentTrack, originalQueue));
            randomizeTracks();
            currentShuffledQueue = new ArrayList<>(shuffledQueue);
            MyMediaPlayer.shuffledIndex = 0;
            layoutManager.scrollToPosition(0);
        } else {
            queueShuffleQueue.setImageResource(R.drawable.shuffle_outline);
            shuffledQueue.clear();
            currentShuffledQueue.clear();
            currentQueue = new ArrayList<>(originalQueue.subList(getTrackOriginalPosition(currentTrack, originalQueue), originalQueue.size()));
            MyMediaPlayer.originalIndex = getTrackOriginalPosition(currentTrack, originalQueue);
        }
        setupPlayer();
    }

    @Override
    public void swapTracks(int i, int j) {
        TrackContainer temp = shuffledQueue.get(i);
        shuffledQueue.set(i, shuffledQueue.get(j));
        shuffledQueue.set(j, temp);
    }

    private void randomizeTracks(){
        Random rand = new Random();
        for (int i = 1; i < shuffledQueue.size(); i++) {
            int j = rand.nextInt(i) + 1;
            swapTracks(i, j);
        }
    }

    @Override
    public void loopQueue() {
        MyMediaPlayer.isLooping = !MyMediaPlayer.isLooping;
        if (MyMediaPlayer.isLooping){
            queueLoopQueue.setImageResource(R.drawable.repeat_fill);
        } else {
            queueLoopQueue.setImageResource(R.drawable.repeat_outline);
        }
    }

    @Override
    public String convertToTimeFormat(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private int getTrackOriginalPosition(TrackContainer trackContainer, ArrayList<TrackContainer> arr){
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getTrack().getName().equals(trackContainer.getTrack().getName())){
                return i;
            }
        }
        return 0;
    }

    private void setupButtonRes(){
        if (MyMediaPlayer.isShuffling){
            queueShuffleQueue.setImageResource(R.drawable.shuffle_fill);
        } else {
            queueShuffleQueue.setImageResource(R.drawable.shuffle_outline);
        }

        if (MyMediaPlayer.isLooping){
            queueLoopQueue.setImageResource(R.drawable.repeat_fill);
        } else {
            queueLoopQueue.setImageResource(R.drawable.repeat_outline);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}