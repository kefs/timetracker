package de.iweinzierl.timetracking.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.fragments.JobStarterFragment;
import de.iweinzierl.timetracking.fragments.StatsFragment;
import de.iweinzierl.timetracking.fragments.TimeTrackerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.utils.Logger;

public class TimeTracker extends Activity implements JobStarterFragment.Callback, StatsFragment.Callback {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case NewCustomerIntent.REQUEST:
                Customer customer = new NewCustomerIntent(data).getCustomer();
                addCustomer(customer);
        }
    }

    @Override
    public void createNewCustomer() {
        Logger.debug(getClass(), "Menu item 'new customer' selected");
        startActivityForResult(new NewCustomerIntent(this), NewCustomerIntent.REQUEST);
    }

    @Override
    public void createNewProject() {
        Logger.debug(getClass(), "Menu item 'new project' selected");
        startActivity(new NewProjectIntent(this));
        // TODO get result of NewCustomerActivity
    }

    @Override
    public void onChangedMode(StatsFragment.Mode oldMode, StatsFragment.Mode newMode) {
        // TODO
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

    private void addCustomer(Customer customer) {
        Logger.debug(getClass(), "Request to save new customer");
        // TODO
    }
}
