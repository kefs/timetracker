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
import android.widget.EditText;
import de.iweinzierl.timetracking.R;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.CustomerBuilder;
import de.iweinzierl.timetracking.utils.Logger;

public class NewCustomerFragment extends Fragment implements TimeTrackerFragment<NewCustomerFragment> {

    public interface Callback {
        void save(Customer customer);

        void cancel();
    }

    private Callback callback;

    @Override
    public NewCustomerFragment create() {
        return new NewCustomerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newcustomer, container, false);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.newcustomer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.newcustomer_save:
                saveCustomer();
                return true;

            case R.id.newcustomer_cancel:
                cancel();
                return true;
        }

        return false;
    }

    private void saveCustomer() {
        Logger.debug(getClass(), "Save new customer");
        Customer customer = null;

        try {
            customer = getCustomer();
        }
        catch (Exception e) {
            Logger.error(getClass(), "Unable to build new Customer", e);
        }

        callback.save(customer);
    }

    private void cancel() {
        Logger.debug(getClass(), "Cancel adding new customer");
        callback.cancel();
    }

    private Customer getCustomer() throws NullPointerException {
        CustomerBuilder builder = new CustomerBuilder();
        builder.setName(getName());

        return builder.build();
    }

    private String getName() {
        View root = getView();
        View name = root != null ? root.findViewById(R.id.newcustomer_name) : null;

        if (name instanceof EditText) {
            return ((EditText) name).getText().toString();
        }

        return null;
    }
}
