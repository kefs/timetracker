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
import de.iweinzierl.timetracking.persistence.repository.RepositoryFactory;
import de.iweinzierl.timetracking.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker extends Activity implements JobStarterFragment.Callback, StatsFragment.Callback,
        HasCustomersChangedListeners, HasProjectsChangedListeners {

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

    private List<ProjectsChangedListener> projectsChangedListeners;
    private List<Project> projects;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersChangedListeners = new ArrayList<CustomersChangedListener>(2);
        projectsChangedListeners = new ArrayList<ProjectsChangedListener>(2);

        customers = new ArrayList<Customer>(0);
        projects= new ArrayList<Project>(0);

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

            case NewProjectIntent.REQUEST:
                Project project = new NewProjectIntent(data).getProject();
                addProject(project);
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
        if (listener != null) {
            customersChangedListeners.add(listener);
        }
    }

    @Override
    public void registerProjectsChangedListener(ProjectsChangedListener listener) {
        if (listener != null) {
            projectsChangedListeners.add(listener);
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
            List<Customer> old = Lists.newCopyOnWriteArrayList(this.customers);
            this.customers = customers;
            notifyCustomersChanged(old, customers);
        }
    }

    private void setProjects(List<Project> projects) {
        if (projects != null) {
            List<Project> old = Lists.newArrayList(this.projects);
            this.projects = projects;
            notifyProjectsChanged(old, projects);
        }
    }

    private void addCustomer(Customer customer) {
        if (customer != null) {
            List<Customer> old = Lists.newCopyOnWriteArrayList(customers);
            customers.add(customer);
            notifyCustomersChanged(old, customers);
        }
    }

    private void addProject(Project project) {
        if (project != null) {
            List<Project> old = Lists.newCopyOnWriteArrayList(projects);
            projects.add(project);
            notifyProjectsChanged(old, projects);
        }
    }

    private void notifyCustomersChanged(List<Customer> oldCustomers, List<Customer> newCustomers) {
        for (CustomersChangedListener listener : customersChangedListeners) {
            listener.onCustomersChanged(oldCustomers, newCustomers);
        }
    }

    private void notifyProjectsChanged(List<Project> oldProjects, List<Project> newProjects) {
        for (ProjectsChangedListener listener: projectsChangedListeners) {
            listener.onProjectsChanged(oldProjects, newProjects);
        }
    }
}
