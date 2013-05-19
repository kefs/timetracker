package de.iweinzierl.timetracking.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.event.ExceptionHandler;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.repository.Repository;
import de.iweinzierl.timetracking.utils.Logger;

public class SaveProjectTask extends AsyncTask<Project, Void, Project> {

    private Context context;
    private Repository repository;

    public SaveProjectTask(Context context, Repository repository) {
        this.context = context;
        this.repository = repository;
    }

    @Override
    protected Project doInBackground(Project... params) {
        if (params == null || params.length == 0) {
            return null;
        }

        try {
            return repository.save(params[0]);
        } catch (DatabaseException e) {
            Logger.error(getClass(), "Unable to save project", e);

            Message message = Message.obtain(new ExceptionHandler(context), ExceptionHandler.WHAT_EXCEPTION);
            message.setData(ExceptionHandler.createBundle(e.getMessage()));

            message.sendToTarget();
        }

        return null;
    }
}
