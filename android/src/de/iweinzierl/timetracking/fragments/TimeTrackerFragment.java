package de.iweinzierl.timetracking.fragments;

import android.app.Fragment;

public interface TimeTrackerFragment<T extends Fragment> {

    T create();
}
