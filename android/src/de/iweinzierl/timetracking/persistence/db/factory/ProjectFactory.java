package de.iweinzierl.timetracking.persistence.db.factory;

import android.database.Cursor;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.domain.ProjectBuilder;

public class ProjectFactory implements DomainFactory<Project> {

    @Override
    public Project create(Cursor cursor) {
        ProjectBuilder builder = new ProjectBuilder();
        setId(cursor, builder);
        setCustomerId(cursor, builder);
        setComment(cursor, builder);
        setIdentifier(cursor, builder);
        setTitle(cursor, builder);

        return builder.build();
    }

    private void setId(Cursor cursor, ProjectBuilder builder) {
        int idx = cursor.getColumnIndex("id");
        if (idx >= 0) {
            builder.setId(cursor.getInt(idx));
        }
    }

    private void setCustomerId(Cursor cursor, ProjectBuilder builder) {
        int idx = cursor.getColumnIndex("customerId");
        if (idx >= 0) {
            builder.setCustomerId(cursor.getInt(idx));
        }
    }

    private void setTitle(Cursor cursor, ProjectBuilder builder) {
        int idx = cursor.getColumnIndex("title");
        if (idx >= 0) {
            builder.setTitle(cursor.getString(idx));
        }
    }

    private void setIdentifier(Cursor cursor, ProjectBuilder builder) {
        int idx = cursor.getColumnIndex("identifier");
        if (idx >= 0) {
            builder.setIdentifier(cursor.getString(idx));
        }
    }

    private void setComment(Cursor cursor, ProjectBuilder builder) {
        int idx = cursor.getColumnIndex("comment");
        if (idx >= 0) {
            builder.setComment(cursor.getString(idx));
        }
    }
}
