package de.iweinzierl.timetracking.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.utils.Logger;

public class StatsFragment extends Fragment implements TimeTrackerFragment<StatsFragment> {

    public static final String TAG = "fragment.tag.stats";

    public enum Mode {
        WEEK,
        MONTH,
        YEAR,
        TOTAL
    }

    public interface Callback {
        void onChangedMode(Mode oldMode, Mode newMode);
    }

    private Mode mode;
    private Callback callback;

    public StatsFragment() {
        mode = Mode.WEEK;
    }

    @Override
    public StatsFragment create() {
        return new StatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if (activity instanceof Callback) {
            this.callback = (Callback) activity;
        } else {
            throw new IllegalArgumentException("Parent Activity must implement Callback");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.statistics, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.statistics_week:
                setMode(Mode.WEEK);
                return true;

            case R.id.statistics_month:
                setMode(Mode.MONTH);
                return true;

            case R.id.statistics_year:
                setMode(Mode.YEAR);
                return true;

            case R.id.statistics_total:
                setMode(Mode.TOTAL);
                return true;
        }

        return false;
    }

    protected void setMode(Mode mode) {
        Logger.info(getClass(), "Change statistics mode to '%s'", mode.name());

        Mode old = this.mode;
        this.mode = mode;

        callback.onChangedMode(old, mode);
    }
}
