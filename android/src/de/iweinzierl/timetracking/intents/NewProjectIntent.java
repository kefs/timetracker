package de.iweinzierl.timetracking.intents;

import android.content.Context;
import android.content.Intent;
import de.iweinzierl.timetracking.activities.NewProjectActivity;
import de.iweinzierl.timetracking.domain.Project;

public class NewProjectIntent extends Intent {

    public static final int REQUEST = 200;

    private static final String EXTRA_PROJECT = "extra.project";

    public NewProjectIntent(Context context) {
        super(context, NewProjectActivity.class);
    }

    public NewProjectIntent(Intent data) {
        super(data);
    }

    public NewProjectIntent(Project project) {
        super();
        putExtra(EXTRA_PROJECT, project);
    }

    public Project getProject() {
        Object obj = getSerializableExtra(EXTRA_PROJECT);
        return obj instanceof Project ? (Project) obj : null;
    }
}
