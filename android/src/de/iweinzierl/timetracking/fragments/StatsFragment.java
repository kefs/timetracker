package de.iweinzierl.timetracking.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.iweinzierl.timetracking.R;

public class StatsFragment extends Fragment implements TimeTrackerFragment<StatsFragment> {

    @Override
    public StatsFragment create() {
        return new StatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }
}
