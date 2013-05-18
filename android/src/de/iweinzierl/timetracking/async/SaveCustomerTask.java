package de.iweinzierl.timetracking.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.repository.Repository;

public class SaveCustomerTask extends AsyncTask<Customer, Void, Customer> {

    private Context context;
    private Repository repository;

    public SaveCustomerTask(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    protected Customer doInBackground(Customer... params) {
        if (params == null || params.length == 0) {
            return null;
        }

        try {
            return repository.save(params[0]);
        } catch (DatabaseException e) {
            Toast.makeText(context, "Unable to save customer", Toast.LENGTH_SHORT);
        }

        return null;
    }
}
