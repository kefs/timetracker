package de.iweinzierl.timetracking.persistence.db.factory;

import android.database.Cursor;
import de.iweinzierl.timetracking.domain.Break;

import java.util.Date;

public class BreakFactory implements DomainFactory<Break> {

    @Override
    public Break create(Cursor cursor) {
        Break b = new Break();

        setId(cursor, b);
        setStart(cursor, b);
        setEnd(cursor, b);
        setComment(cursor, b);

        return b;
    }

    private static void setComment(Cursor cursor, Break b) {
        int idxComment = cursor.getColumnIndex("comment");
        if (idxComment >= 0) {
            b.setComment(cursor.getString(idxComment));
        }
    }

    private static void setEnd(Cursor cursor, Break b) {
        int idxEnd = cursor.getColumnIndex("end");
        if (idxEnd >= 0) {
            b.setEnd(new Date(cursor.getInt(idxEnd)));
        }
    }

    private static void setStart(Cursor cursor, Break b) {
        int idxStart = cursor.getColumnIndex("start");
        if (idxStart >= 0) {
            b.setStart(new Date(cursor.getInt(idxStart)));
        }
    }

    private static void setId(Cursor cursor, Break b) {
        int idxId = cursor.getColumnIndex("id");
        if (idxId >= 0) {
            b.setId(cursor.getLong(idxId));
        }
    }
}
