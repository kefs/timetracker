package de.iweinzierl.timetracking.async;

import android.content.Context;
import android.os.AsyncTask;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.persistence.repository.Repository;

import java.util.List;

public class LoadCustomerTask extends AsyncTask<Void, Void, List<Customer>> {

    private Context context;
    private Repository repository;

    public LoadCustomerTask(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    protected List<Customer> doInBackground(Void... params) {
        return repository.listCustomers();
    }
}
