package de.iweinzierl.timetracking.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.fragments.JobStarterFragment;
import de.iweinzierl.timetracking.fragments.StatsFragment;
import de.iweinzierl.timetracking.fragments.TimeTrackerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.utils.Logger;

public class TimeTracker extends Activity implements JobStarterFragment.Callback {

    public static class TabListener<T extends TimeTrackerFragment> implements ActionBar.TabListener {

        private Fragment fragment;
        private Class<T> clazz;

        public TabListener(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            try {
                fragment = clazz.newInstance().create();
                ft.replace(android.R.id.content, fragment);
            } catch (Exception e) {
                Logger.error(getClass(), "Unable to create instance of " + clazz.toString(), e);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // nothing to do here
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar(getActionBar());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.jobstarter_newcustomer:
                createNewCustomer();
                return true;

            case R.id.jobstarter_newproject:
                createNewProject();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNewCustomer() {
        Logger.debug(getClass(), "Menu item 'new customer' selected");
        startActivity(new NewCustomerIntent(this));
        // TODO get result of NewCustomerActivity
    }

    public void createNewProject() {
        Logger.debug(getClass(), "Menu item 'new project' selected");
        startActivity(new NewProjectIntent(this));
        // TODO get result of NewCustomerActivity
    }

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab startStop = actionBar.newTab().setText(R.string.main_tab_startstop).setTabListener(
                new TabListener<JobStarterFragment>(JobStarterFragment.class));
        ActionBar.Tab statistics = actionBar.newTab().setText(R.string.main_tab_statistics).setTabListener(
                new TabListener<StatsFragment>(StatsFragment.class));

        actionBar.addTab(startStop);
        actionBar.addTab(statistics);
    }
}
