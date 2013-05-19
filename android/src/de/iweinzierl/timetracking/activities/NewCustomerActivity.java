package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.iweinzierl.timetracking.async.SaveCustomerTask;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.fragments.NewCustomerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;
import de.iweinzierl.timetracking.persistence.repository.RepositoryFactory;

public class NewCustomerActivity extends Activity implements  NewCustomerFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new NewCustomerFragment().create())
                .commit();
    }

    @Override
    public void save(Customer customer) {
        new SaveCustomerTask(this, RepositoryFactory.create(this)) {
            @Override
            public void onPostExecute(Customer saved) {
                Intent i = new NewCustomerIntent(saved);
                setResult(Activity.RESULT_OK, i);
                finish();

            }
        }.execute(customer);
    }

    @Override
    public void cancel() {
        finish();
    }
}
