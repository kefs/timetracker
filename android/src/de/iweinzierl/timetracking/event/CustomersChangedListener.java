package de.iweinzierl.timetracking.event;

import de.iweinzierl.timetracking.domain.Customer;

public interface CustomersChangedListener {

    void onCustomerAdded(Customer customer);
}
