package de.iweinzierl.timetracking.intents;

import android.content.Context;
import android.content.Intent;
import de.iweinzierl.timetracking.activities.NewCustomerActivity;
import de.iweinzierl.timetracking.domain.Customer;

public class NewCustomerIntent extends Intent {

    public static final int REQUEST = 100;

    public static final String EXTRA_CUSTOMER = "extra.customer";

    public NewCustomerIntent(Context packageContext) {
        super(packageContext, NewCustomerActivity.class);
    }

    public NewCustomerIntent(Intent i) {
        super(i);
    }

    public NewCustomerIntent(Customer customer) {
        super();
        setCustomer(customer);
    }

    public void setCustomer(Customer customer) {
        putExtra(EXTRA_CUSTOMER, customer);
    }

    public Customer getCustomer() {
        Object obj = getSerializableExtra(EXTRA_CUSTOMER);
        return obj instanceof Customer ? (Customer) obj : null;
    }
}

