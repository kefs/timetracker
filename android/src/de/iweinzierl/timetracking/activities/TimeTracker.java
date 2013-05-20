package de.iweinzierl.timetracking.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import com.google.common.collect.Lists;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.event.CustomersChangedListener;
import de.iweinzierl.timetracking.event.HasCustomersChangedListeners;
import de.iweinzierl.timetracking.event.HasProjectsChangedListeners;
import de.iweinzierl.timetracking.event.ProjectsChangedListener;
import de.iweinzierl.timetracking.fragments.JobStarterFragment;
import de.iweinzierl.timetracking.fragments.StatsFragment;
import de.iweinzierl.timetracking.fragments.TimeTrackerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker extends Activity implements JobStarterFragment.Callback, StatsFragment.Callback,
        HasCustomersChangedListeners, HasProjectsChangedListeners {

    public static class TabListener<T extends TimeTrackerFragment> implements ActionBar.TabListener {

        private Fragment fragment;
        private Class<T> clazz;
        private String tag;

        public TabListener(Class<T> clazz, String tag) {
            this.clazz = clazz;
            this.tag = tag;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            try {
                if (fragment == null) {
                    fragment = clazz.newInstance().create();
                    ft.add(android.R.id.content, fragment, tag);
                } else {
                    ft.attach(fragment);
                }

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
            if (fragment != null) {
                ft.attach(fragment);
            }
        }
    }

    private List<CustomersChangedListener> customersChangedListeners;
    private List<ProjectsChangedListener> projectsChangedListeners;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersChangedListeners = new ArrayList<CustomersChangedListener>(2);
        projectsChangedListeners = new ArrayList<ProjectsChangedListener>(2);

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
                addedCustomer(customer);

            case NewProjectIntent.REQUEST:
                Project project = new NewProjectIntent(data).getProject();
                addedProject(project);
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
        startActivityForResult(new NewProjectIntent(this), NewProjectIntent.REQUEST);
    }

    @Override
    public void onChangedMode(StatsFragment.Mode oldMode, StatsFragment.Mode newMode) {
        // TODO
    }

    @Override
    public void registerCustomersChangedListener(CustomersChangedListener listener) {
        if (listener != null && customersChangedListeners.indexOf(listener) < 0) {
            customersChangedListeners.add(listener);
        }
    }

    @Override
    public void registerProjectsChangedListener(ProjectsChangedListener listener) {
        if (listener != null && projectsChangedListeners.indexOf(listener) < 0) {
            projectsChangedListeners.add(listener);
        }
    }

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab startStop = actionBar.newTab().setText(R.string.main_tab_startstop).setTabListener(
                new TabListener<JobStarterFragment>(JobStarterFragment.class, JobStarterFragment.TAG));
        ActionBar.Tab statistics = actionBar.newTab().setText(R.string.main_tab_statistics).setTabListener(
                new TabListener<StatsFragment>(StatsFragment.class, StatsFragment.TAG));

        actionBar.addTab(startStop);
        actionBar.addTab(statistics);
    }

    private void addedCustomer(Customer customer) {
        if (customer != null) {
            notifyCustomerAdded(customer);
        }
    }

    private void addedProject(Project project) {
        notifyProjectAdded(project);
    }

    private void notifyCustomerAdded(Customer customer) {
        for (CustomersChangedListener listener : customersChangedListeners) {
            listener.onCustomerAdded(customer);
        }
    }

    private void notifyProjectAdded(Project project) {
        for (ProjectsChangedListener listener : projectsChangedListeners) {
            listener.onProjectAdded(project);
        }
    }
}
