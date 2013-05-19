package de.iweinzierl.timetracking.async;

import android.content.Context;
import android.os.AsyncTask;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.persistence.repository.Repository;

import java.util.Collections;
import java.util.List;

public class LoadProjectsTask extends AsyncTask<Integer, Void, List<Project>> {

    private Context context;
    private Repository repository;

    public LoadProjectsTask(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    protected List<Project> doInBackground(Integer... params) {
        if (params != null && params.length > 0) {
            return repository.listProjects(params[0]);
        }

        return Collections.emptyList();
    }
}
