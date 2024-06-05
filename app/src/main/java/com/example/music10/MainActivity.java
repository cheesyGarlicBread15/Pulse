package com.example.music10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.music10.Fragments.FragContainer2.MiniPlayerFragment;
import com.example.music10.Fragments.FragContainer2.QueueFragment;
import com.example.music10.Fragments.FragContainer.SongsFragment;
import com.example.music10.Fragments.PlaylistDetailsFragment;
import com.example.music10.Interfaces.MiniPlayerFragmentListener;
import com.example.music10.Interfaces.OnSecondContainerChangeFragment;
import com.example.music10.Interfaces.QueueFragmentListener;
import com.example.music10.Player.MyMediaPlayer;
import com.example.music10.Playlist.Schema.PlaylistDbHelper;
import com.example.music10.Track.Track;
import com.example.music10.Track.TrackContainer;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSecondContainerChangeFragment, QueueFragmentListener, MiniPlayerFragmentListener {
    TabLayout tabLayout;
    public static FragmentContainerView fragmentContainerView, fragmentContainerViewPlaylist;
    public static RelativeLayout.LayoutParams layoutParams;
    int selectedTab = 1;
    public static ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    public static PlaylistDbHelper dbHelper;
    public static final int PERMISSION_REQUEST_CODE = 100;


    // TODO: 09/05/2024 FORMAT TEXT, pili ug chad nga font
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.tablayout);
        fragmentContainerView = findViewById(R.id.fragment_container_for_queue);
        fragmentContainerViewPlaylist = findViewById(R.id.fragment_container_for_playlist);
        viewPager2 = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);

        fragmentContainerView.setOnClickListener(this);

        dbHelper = new PlaylistDbHelper(getApplicationContext());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        selectTab(selectedTab);

        initSongs();
        dbHelper.createFavoritePlaylistIfNotExists();
    }

    private void selectTab(int tabNumber){
        layoutParams = (RelativeLayout.LayoutParams) fragmentContainerView.getLayoutParams();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.enter_bottom_to_top,
                R.anim.exit_top_to_bottom,
                R.anim.enter_bottom_to_top,
                R.anim.exit_top_to_bottom
        );
        switch (tabNumber){
            case 4:
                QueueFragment queueFragment = (QueueFragment) getSupportFragmentManager().findFragmentByTag("QueueFragment");
                if (queueFragment != null && queueFragment.getView() == null){
                    transaction.replace(R.id.fragment_container_for_queue, queueFragment, "QueueFragment");
                } else {
                    transaction.replace(R.id.fragment_container_for_queue, new QueueFragment(), "QueueFragment");
                }
                transaction.addToBackStack(null).commit();

                // TODO: 03/05/2024 fix animation visual bug, now playing appears in top right

                fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        if (f instanceof QueueFragment){
                            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
                            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                            fragmentContainerView.setLayoutParams(layoutParams);
                        }
                    }
                }, false);
                break;
            case 5:
                transaction.replace(R.id.fragment_container_for_queue, new MiniPlayerFragment(), "MiniPlayerFragment")
                        .addToBackStack(null)
                        .commit();

                fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        fragmentContainerView.setLayoutParams(layoutParams);
                    }
                }, false);
                break;
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_for_queue);
        if (fragment instanceof QueueFragment){
            Handler handler = new Handler();
            handler.removeCallbacksAndMessages(null);
            Fragment queueFragment = getSupportFragmentManager().findFragmentByTag("QueueFragment");
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_bottom_to_top, R.anim.exit_top_to_bottom)
                    .remove(queueFragment)
                    .commit();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    MainActivity.fragmentContainerView.setLayoutParams(MainActivity.layoutParams);
                }
            }, 500);
            secondContainerChangeFragment(5);
        } else if (fragment instanceof MiniPlayerFragment && fragmentContainerViewPlaylist.getVisibility() == View.VISIBLE) {
            Fragment playlistDetailsFragment = getSupportFragmentManager().findFragmentByTag("PlaylistDetailsFragment");
            getSupportFragmentManager().beginTransaction()
                    .remove(playlistDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_container_for_queue){
            secondContainerChangeFragment(4);
        }
    }


    @Override
    public void secondContainerChangeFragment(int tabnumber) {
        if (fragmentContainerView.getVisibility() == View.GONE){
            fragmentContainerView.setElevation(1);
            fragmentContainerView.setVisibility(View.VISIBLE);
        }
        selectTab(tabnumber);
    }

    @Override
    public void onQueueFragmentReady() {
        QueueFragment fragment = ((QueueFragment) getSupportFragmentManager().findFragmentByTag("QueueFragment"));

        if (MyMediaPlayer.onAllSongs){
            fragment.setOriginalQueue(SongsFragment.allTracks);
            MyMediaPlayer.onAllSongs = false;
        } else if (MyMediaPlayer.onPlaylist) {
//                fragment.setOriginalQueue(((PlaylistDetailsFragment) getSupportFragmentManager().findFragmentByTag("PlaylistDetailsFragment")));
            fragment.setOriginalQueue(PlaylistDetailsFragment.playlistTracks);
            MyMediaPlayer.onPlaylist = false;
            }

    }

    @Override
    public void onMiniPlayerFragmentReady() {
        ((MiniPlayerFragment) getSupportFragmentManager().findFragmentByTag("MiniPlayerFragment")).showCurrentPlayingTrack(QueueFragment.currentTrack);

    }

    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED, PLEASE ALLOW FROM SETTINGS", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void initSongs(){
        if (checkPermission() == false){
            requestPermission();
        } else {
            String[] projection = {
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
            };
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
            Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
            while (cursor.moveToNext()){
                TrackContainer trackContainer = new TrackContainer(new Track(
                        // TODO: 24/04/2024 if there are not arist or album (index 1 and 2), set it to empty string
                        cursor.getString(0),
                        cursor.getString(1) == null || cursor.getString(1).isEmpty() || cursor.getString(1).equals("<unknown>") ? "" : cursor.getString(1),
                        cursor.getString(2) == null || cursor.getString(2).isEmpty() || cursor.getString(2).equals("Music")? "" : cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        R.drawable.music
                ));
                if (new File(trackContainer.getTrack().getPath()).exists()){
                    SongsFragment.allTracks.add(trackContainer);
                }
            }
            if (SongsFragment.allTracks.isEmpty()){
                Toast.makeText(this, "No Songs!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}