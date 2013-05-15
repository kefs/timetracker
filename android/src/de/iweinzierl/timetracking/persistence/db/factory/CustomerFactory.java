package de.iweinzierl.timetracking.persistence.db.factory;

import android.database.Cursor;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.CustomerBuilder;

public class CustomerFactory implements DomainFactory<Customer> {

    @Override
    public Customer create(Cursor cursor) {
        CustomerBuilder builder = new CustomerBuilder();
        setName(cursor, builder);
        setId(cursor, builder);

        return builder.build();
    }

    private void setName(Cursor cursor, CustomerBuilder builder) {
        int idx = cursor.getColumnIndex("name");
        if (idx >= 0) {
            builder.setName(cursor.getString(idx));
        }
    }

    private void setId(Cursor cursor, CustomerBuilder builder) {
        int idx = cursor.getColumnIndex("id");
        if (idx >= 0) {
            builder.setId(cursor.getInt(idx));
        }
    }
}
