package de.iweinzierl.timetracking.intents;

import android.content.Context;
import android.content.Intent;
import de.iweinzierl.timetracking.activities.NewCustomerActivity;

public class NewCustomerIntent extends Intent {

    public NewCustomerIntent(Context packageContext) {
        super(packageContext, NewCustomerActivity.class);
    }
}

