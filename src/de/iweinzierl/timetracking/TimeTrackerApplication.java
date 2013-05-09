package de.iweinzierl.timetracking;

import android.app.Application;
import de.iweinzierl.timetracking.persistence.RepositoryFactory;
import de.iweinzierl.timetracking.persistence.repository.Repository;

public class TimeTrackerApplication extends Application {

    private Repository repository;

    public TimeTrackerApplication()  {
        repository = RepositoryFactory.create(getApplicationContext());
    }
}
