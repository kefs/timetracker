package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.os.Bundle;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.fragments.NewProjectFragment;

public class NewProjectActivity extends Activity implements NewProjectFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new NewProjectFragment().create())
                .commit();
    }

    @Override
    public void save(Project project) {
        // TODO
    }

    @Override
    public void cancel() {
        // TODO
    }
}
