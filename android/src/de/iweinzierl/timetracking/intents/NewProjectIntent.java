package de.iweinzierl.timetracking.intents;

import android.content.Context;
import android.content.Intent;
import de.iweinzierl.timetracking.activities.NewProjectActivity;

public class NewProjectIntent extends Intent {

    public NewProjectIntent(Context context) {
        super(context, NewProjectActivity.class);
    }
}
