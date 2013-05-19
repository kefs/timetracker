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
import android.widget.AdapterView;
import android.widget.Spinner;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.async.LoadCustomerTask;
import de.iweinzierl.timetracking.async.LoadProjectsTask;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.event.CustomersChangedListener;
import de.iweinzierl.timetracking.event.HasCustomersChangedListeners;
import de.iweinzierl.timetracking.event.HasProjectsChangedListeners;
import de.iweinzierl.timetracking.event.ProjectsChangedListener;
import de.iweinzierl.timetracking.persistence.repository.RepositoryFactory;
import de.iweinzierl.timetracking.utils.Logger;
import de.iweinzierl.timetracking.widgets.CustomerAdapter;
import de.iweinzierl.timetracking.widgets.ProjectAdapter;

import java.util.Collections;
import java.util.List;

/**
 * New Fragment that will display selectable fields for <i>customer</i> and <i>project</i> and a button to start a
 * new <i>job</i>.
 */
public class JobStarterFragment extends Fragment implements TimeTrackerFragment<JobStarterFragment>,
        CustomersChangedListener, ProjectsChangedListener {

    private static final String SAVED_CUSTOMER_POSITION = "bundle.customer.position";
    private static final String SAVED_PROJECT_POSITION = "bundle.project.position";

    public static final String TAG = "fragment.tag.jobstarter";

    public interface Callback {
        void createNewCustomer();

        void createNewProject();
    }

    // FIELDS

    private Callback callback;

    private CustomerAdapter customerAdapter;
    private ProjectAdapter projectAdapter;

    private int restoredCustomerPosition = -1;
    private int restoredProjectPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            restoredCustomerPosition = savedInstanceState.getInt(SAVED_CUSTOMER_POSITION);
            restoredProjectPosition = savedInstanceState.getInt(SAVED_PROJECT_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.debug(getClass(), "JobStarterFragment.onCreateView()");
        return inflater.inflate(R.layout.fragment_jobstarter, container, false);
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

        if (activity instanceof HasCustomersChangedListeners) {
            ((HasCustomersChangedListeners) activity).registerCustomersChangedListener(this);
        }

        if (activity instanceof HasProjectsChangedListeners) {
            ((HasProjectsChangedListeners) activity).registerProjectsChangedListener(this);
        }

        setupAdapters();
        loadCustomers();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
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

    @Override
    public JobStarterFragment create() {
        return new JobStarterFragment();
    }

    @Override
    public void onCustomersChanged(List<Customer> oldCustomers, List<Customer> newCustomers) {
        customerAdapter.setCustomers(newCustomers);
    }

    @Override
    public void onProjectsChanged(List<Project> oldProjects, List<Project> newProjects) {
        projectAdapter.setProjects(newProjects);
    }

    private void setupAdapters() {
        View layout = getView();

        this.customerAdapter = new CustomerAdapter(getActivity());
        this.projectAdapter = new ProjectAdapter(getActivity());

        Spinner customerSpinner = getSpinner(layout, R.id.customer_selector);
        Spinner projectSpinner = getSpinner(layout, R.id.project_selector);

        if (customerSpinner != null) {
            customerSpinner.setAdapter(customerAdapter);
            customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    loadProjects(((Customer) customerAdapter.getItem(position)).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    clearProjects();
                }
            });
        }

        if (projectSpinner != null) {
            projectSpinner.setAdapter(projectAdapter);
        }
    }

    private void saveState(Bundle bundle) {
        restoredCustomerPosition = getCustomerPosition();
        restoredProjectPosition = getProjectPosition();

        if (bundle != null) {
            bundle.putInt(SAVED_CUSTOMER_POSITION, getCustomerPosition());
            bundle.putInt(SAVED_PROJECT_POSITION, getProjectPosition());
        }
    }

    private Spinner getSpinner(View container, int resId) {
        View spinner = container.findViewById(resId);
        return spinner instanceof Spinner ? (Spinner) spinner : null;
    }

    private void loadCustomers() {
        new LoadCustomerTask(getActivity(), RepositoryFactory.create(getActivity())) {
            @Override
            public void onPostExecute(List<Customer> customers) {
                customerAdapter.setCustomers(customers);

                if (restoredCustomerPosition >= 0) {
                    setCustomerPosition(restoredCustomerPosition);
                    restoredCustomerPosition = -1;
                }
            }
        }.execute();
    }

    private void loadProjects(int customerId) {
        new LoadProjectsTask(getActivity(), RepositoryFactory.create(getActivity())) {
            @Override
            protected void onPostExecute(List<Project> projects) {
                projectAdapter.setProjects(projects);

                if (restoredProjectPosition >= 0) {
                    setProjectPosition(restoredProjectPosition);
                    restoredProjectPosition = -1;
                }
            }
        }.execute(customerId);
    }

    private void clearProjects() {
        projectAdapter.setProjects(Collections.<Project>emptyList());
    }

    private int getCustomerPosition() {
        Spinner customer = getSpinner(getView(), R.id.customer_selector);
        return customer != null ? customer.getSelectedItemPosition() : -1;
    }

    private void setCustomerPosition(int position) {
        Spinner customer = getSpinner(getView(), R.id.customer_selector);
        if (customer != null) {
            customer.setSelection(position);
        }
    }

    private int getProjectPosition() {
        Spinner project = getSpinner(getView(), R.id.project_selector);
        return project != null ? project.getSelectedItemPosition() : -1;
    }

    private void setProjectPosition(int position) {
        Spinner project = getSpinner(getView(), R.id.project_selector);
        if (project != null) {
            project.setSelection(position);
        }
    }
}
