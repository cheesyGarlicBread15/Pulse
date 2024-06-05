package com.example.music10;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music10.Fragments.FavoritesFragment;
import com.example.music10.Fragments.FragContainer.PlaylistsFragment;
import com.example.music10.Fragments.FragContainer.SongsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SongsFragment();
            case 1:
                return new PlaylistsFragment();
            case 2:
                return new FavoritesFragment();
            default:
                return new SongsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
