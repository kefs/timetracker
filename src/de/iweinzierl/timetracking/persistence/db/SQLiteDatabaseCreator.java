package de.iweinzierl.timetracking.persistence.db;

import android.database.sqlite.SQLiteDatabase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SQLiteDatabaseCreator {

    public static final String TBL_CUSTOMER = "customer";
    public static final String TBL_PROJECT = "project";
    public static final String TBL_JOB = "job";
    public static final String TBL_BREAK = "break";

    public static final String[] COLS_BREAK = new String[] {"id", "job_id", "start", "end", "comment"};
    public static final String[] COLS_JOB = new String[] {"id", "project_id", "start", "end", "title", "comment"};
    public static final String[] COLS_PROJECT = new String[] {"id", "customer_id", "title", "identifier", "comment"};
    public static final String[] COLS_CUSTOMER = new String[] {"id", "name"};

    public static final String CREATE_TBL_BREAK = "" +
        "CREATE TABLE " + TBL_BREAK + " (" +
            "id      LONG PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "job_id  LONG NOT NULL," +
            "start   INTEGER NOT NULL," +
            "end     INTEGER NOT NULL," +
            "comment TEXT," +
            "FOREIGN KEY (job_id) REFERENCES job(id)" +
        ");";

    public static final String CREATE_TBL_JOB = "" +
        "CREATE TABLE " + TBL_JOB + " (" +
            "id          LONG PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "project_id  LONG NOT NULL," +
            "start       INTEGER NOT NULL," +
            "end         INTEGER NOT NULL," +
            "title       VARCHAR(64)," +
            "comment     TEXT," +
            "FOREIGN KEY (project_id) REFERENCES project(id)" +
        ");";

    public static final String CREATE_TBL_PROJECT = "" +
        "CREATE TABLE " + TBL_PROJECT + " (" +
            "id          LONG PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "customer_id LONG NOT NULL," +
            "title       VARCHAR(64) NOT NULL," +
            "identifier  VARCHAR(64)," +
            "comment     TEXT," +
            "FOREIGN KEY (customer_id) REFERENCES customer(id)" +
        ");";

    public static final String CREATE_TBL_CUSTOMER = "" +
        "CREATE TABLE " + TBL_CUSTOMER + " (" +
            "id      LONG PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "name    VARCHAR(64) NOT NULL" +
        ");";

    private SQLiteDatabase db;

    public SQLiteDatabaseCreator(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Create schema for TimeTracker application. Following tables are created:</br>
     * <ul>
     *     <li>customer</li>
     *     <li>project</li>
     *     <li>job</li>
     *     <li>break</li>
     * </ul>
     */
    public void createSchema() {
        createCustomerTable();
        createProjectTable();
        createJobTable();
        createBreakTable();
    }

    public void updateSchema() {
        throw new NotImplementedException();
    }

    public void createBreakTable() {
        db.execSQL(CREATE_TBL_BREAK);
    }

    public void createJobTable() {
        db.execSQL(CREATE_TBL_JOB);
    }

    public void createProjectTable() {
        db.execSQL(CREATE_TBL_PROJECT);
    }

    public void createCustomerTable() {
        db.execSQL(CREATE_TBL_CUSTOMER);
    }
}
