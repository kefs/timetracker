package de.iweinzierl.timetracking.event;

public interface HasCustomersChangedListeners {

    void registerCustomersChangedListener(CustomersChangedListener listener);
}
