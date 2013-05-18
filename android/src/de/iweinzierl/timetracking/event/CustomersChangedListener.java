package de.iweinzierl.timetracking.event;

import de.iweinzierl.timetracking.domain.Customer;

import java.util.List;

public interface CustomersChangedListener {
    void onCustomersChanged(List<Customer> oldCustomers, List<Customer> newCustomers);
}
