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

/**
 * New Fragment that will display selectable fields for <i>customer</i> and <i>project</i> and a button to start a
 * new <i>job</i>.
 */
public class JobStarterFragment extends Fragment {

    public interface Callback {
        void createNewCustomer();
        void createNewProject();
    }

    // FIELDS

    private Callback callback;

    public static final JobStarterFragment createNew() {
        return new JobStarterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.debug(getClass(), "JobStarterFragment.onCreateView()");

        View layout = inflater.inflate(R.layout.fragment_jobstarter, container, false);

        // TODO add listeners and co

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        Activity activity = getActivity();
        if (activity instanceof Callback) {
            this.callback = (Callback) activity;
        }
        else {
            throw new IllegalArgumentException("Parent Activity must implement Callback");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.jobstarter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.jobstarter_newcustomer:
                callback.createNewCustomer();
                return true;

            case R.id.jobstarter_newproject:
                callback.createNewProject();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
