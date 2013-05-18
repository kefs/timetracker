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
import android.widget.Spinner;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.event.CustomersChangedListener;
import de.iweinzierl.timetracking.event.HasCustomersChangedListeners;
import de.iweinzierl.timetracking.utils.Logger;
import de.iweinzierl.timetracking.widgets.CustomerAdapter;
import de.iweinzierl.timetracking.widgets.ProjectAdapter;

import java.util.List;

/**
 * New Fragment that will display selectable fields for <i>customer</i> and <i>project</i> and a button to start a
 * new <i>job</i>.
 */
public class JobStarterFragment extends Fragment implements TimeTrackerFragment<JobStarterFragment>, CustomersChangedListener {

    public interface Callback {
        void createNewCustomer();

        void createNewProject();
    }

    // FIELDS

    private Callback callback;
    private CustomerAdapter customerAdapter;
    private ProjectAdapter projectAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.debug(getClass(), "JobStarterFragment.onCreateView()");

        View layout = inflater.inflate(R.layout.fragment_jobstarter, container, false);

        return layout;
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

        setupAdapters();
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

    private void setupAdapters() {
        View layout = getView();

        this.customerAdapter = new CustomerAdapter(getActivity());
        this.projectAdapter = new ProjectAdapter(getActivity());

        Spinner customerSpinner = getSpinner(layout, R.id.customer_selector);
        Spinner projectSpinner = getSpinner(layout, R.id.project_selector);

        if (customerSpinner != null) {
            customerSpinner.setAdapter(customerAdapter);
        }

        if (projectSpinner != null) {
            projectSpinner.setAdapter(projectAdapter);
        }
    }

    private Spinner getSpinner(View container, int resId) {
        View spinner = container.findViewById(resId);
        return spinner instanceof Spinner ? (Spinner) spinner : null;
    }
}
