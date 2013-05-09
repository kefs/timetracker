package de.iweinzierl.timetracking.persistence;

import android.content.Context;
import de.iweinzierl.timetracking.persistence.repository.Repository;
import de.iweinzierl.timetracking.persistence.repository.SQLiteRepository;

public class RepositoryFactory {

    public static Repository create(final Context context) {
        return new SQLiteRepository(context);
    }
}
