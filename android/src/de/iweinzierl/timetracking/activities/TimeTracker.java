package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.fragments.JobStarterFragment;
import de.iweinzierl.timetracking.fragments.WeekStatsFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.utils.Logger;

public class TimeTracker extends Activity implements JobStarterFragment.Callback, WeekStatsFragment.Callback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_jobstarter, JobStarterFragment.createNew());
            addFragment(R.id.fragment_weekstats, WeekStatsFragment.createNew());
        }
    }

    @Override
    public void createNewCustomer() {
        Logger.debug(getClass(), "Menu item 'new customer' selected");
        startActivity(new NewCustomerIntent(this));
        // TODO get result of NewCustomerActivity
    }

    @Override
    public void createNewProject() {
        Logger.debug(getClass(), "Menu item 'new project' selected");
        startActivity(new NewProjectIntent(this));
        // TODO get result of NewCustomerActivity
    }

    private void addFragment(int containerId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerId, fragment);
        fragmentTransaction.commit();
    }
}
