package de.iweinzierl.timetracking.persistence.db.factory;

import android.database.Cursor;

public interface DomainFactory<T> {

    T create(Cursor cursor);
}
