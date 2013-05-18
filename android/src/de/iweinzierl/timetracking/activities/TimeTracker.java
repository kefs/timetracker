package de.iweinzierl.timetracking.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.google.common.collect.Lists;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.async.LoadCustomerTask;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.event.CustomersChangedListener;
import de.iweinzierl.timetracking.event.HasCustomersChangedListeners;
import de.iweinzierl.timetracking.fragments.JobStarterFragment;
import de.iweinzierl.timetracking.fragments.StatsFragment;
import de.iweinzierl.timetracking.fragments.TimeTrackerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.persistence.repository.RepositoryFactory;
import de.iweinzierl.timetracking.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker extends Activity implements JobStarterFragment.Callback, StatsFragment.Callback, HasCustomersChangedListeners {

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

    private List<CustomersChangedListener> customersChangedListeners;
    private List<Customer> customers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersChangedListeners = new ArrayList<CustomersChangedListener>(2);

        setupActionBar(getActionBar());
    }

    @Override
    public void onStart() {
        super.onStart();

        new LoadCustomerTask(this, RepositoryFactory.create(this)) {
            @Override
            public void onPostExecute(List<Customer> customers) {
                setCustomers(customers);
            }
        }.execute();
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

    @Override
    public void registerCustomersChangedListener(CustomersChangedListener listener) {
        if (listener != null) {
            customersChangedListeners.add(listener);
        }
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

    private void setCustomers(List<Customer> customers) {
        if (customers != null) {
            List<Customer> old = Lists.newCopyOnWriteArrayList(customers);
            this.customers = customers;
            notifyCustomersChanged(old, customers);
        }
    }

    private void addCustomer(Customer customer) {
        if (customer != null) {
            List<Customer> old = Lists.newCopyOnWriteArrayList(customers);
            customers.add(customer);
            notifyCustomersChanged(old, customers);
        }
    }

    private void notifyCustomersChanged(List<Customer> oldCustomers, List<Customer> newCustomers) {
        for (CustomersChangedListener listener : customersChangedListeners) {
            listener.onCustomersChanged(oldCustomers, newCustomers);
        }
    }
}
