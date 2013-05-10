package de.iweinzierl.timetracking.persistence.db.factory;

import android.database.Cursor;
import de.iweinzierl.timetracking.domain.Job;

import java.util.Date;

public class JobFactory implements DomainFactory<Job> {

    @Override
    public Job create(Cursor cursor) {
        Job job = new Job();

        setId(cursor, job);
        setTitle(cursor, job);
        setComment(cursor, job);
        setStart(cursor, job);
        setStop(cursor, job);

        return job;
    }

    private void setStop(Cursor cursor, Job job) {
        int idx = cursor.getColumnIndex("stop");
        if (idx >= 0) {
            job.setStopTime(new Date(cursor.getInt(idx)));
        }
    }

    private void setStart(Cursor cursor, Job job) {
        int idx = cursor.getColumnIndex("start");
        if (idx >= 0) {
            job.setStartTime(new Date(cursor.getInt(idx)));
        }
    }

    private void setComment(Cursor cursor, Job job) {
        int idx = cursor.getColumnIndex("comment");
        if (idx >= 0) {
            job.setComment(cursor.getString(idx));
        }
    }

    private void setTitle(Cursor cursor, Job job) {
        int idx = cursor.getColumnIndex("title");
        if (idx >= 0) {
            job.setTitle(cursor.getString(idx));
        }
    }

    private void setId(Cursor cursor, Job job) {
        int idx = cursor.getColumnIndex("id");
        if (idx >= 0) {
            job.setId(cursor.getLong(idx));
        }
    }
}
