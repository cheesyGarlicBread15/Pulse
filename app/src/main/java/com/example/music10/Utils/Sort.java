package com.example.music10.Utils;

import com.example.music10.Track.Track;
import com.example.music10.Track.TrackContainer;

import java.util.ArrayList;
import java.util.Comparator;

public class Sort {
    public static void sortObjects(ArrayList<TrackContainer> arr, Comparator<Track> comparator){
        int n = arr.size();
        for (int i = 1; i < n - 1; i++) {
            TrackContainer current = arr.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(arr.get(j).getTrack(), current.getTrack()) > 0) {
                arr.set(j + 1, arr.get(j));
                j -= 1;
            }
            arr.set(j + 1, current);
        }
    }
}
