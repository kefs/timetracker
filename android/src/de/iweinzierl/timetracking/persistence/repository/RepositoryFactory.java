package de.iweinzierl.timetracking.persistence.repository;

import android.content.Context;
import de.iweinzierl.timetracking.persistence.repository.Repository;
import de.iweinzierl.timetracking.persistence.repository.SQLiteRepository;

public class RepositoryFactory {

    public static Repository create(final Context context) {
        return SQLiteRepository.getInstance(context);
    }
}
