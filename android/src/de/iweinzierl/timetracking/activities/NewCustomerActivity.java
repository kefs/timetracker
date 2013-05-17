package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.fragments.NewCustomerFragment;
import de.iweinzierl.timetracking.intents.NewCustomerIntent;

public class NewCustomerActivity extends Activity implements  NewCustomerFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new NewCustomerFragment().create())
                .commit();
    }

    @Override
    public void save(Customer customer) {
        Intent i = new NewCustomerIntent(customer);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public void cancel() {
        finishActivity(Activity.RESULT_CANCELED);
    }
}
