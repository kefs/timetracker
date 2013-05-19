package de.iweinzierl.timetracking.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import de.iweinzierl.timetracking.async.SaveProjectTask;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.fragments.NewProjectFragment;
import de.iweinzierl.timetracking.intents.NewProjectIntent;
import de.iweinzierl.timetracking.persistence.repository.RepositoryFactory;

public class NewProjectActivity extends Activity implements NewProjectFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new NewProjectFragment().create())
                .commit();
    }

    @Override
    public void save(Project project) {
        new SaveProjectTask(this, RepositoryFactory.create(this)) {
            @Override
            protected void onPostExecute(Project project) {
                super.onPostExecute(project);

                Intent i = new NewProjectIntent(project);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        }.execute(project);
    }

    @Override
    public void cancel() {
        finish();
    }
}
