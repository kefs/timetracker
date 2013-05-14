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

    public static final String[] TABLES = { TBL_CUSTOMER, TBL_PROJECT, TBL_JOB, TBL_BREAK };

    public static final String DROP_TABLE_TEMPLATE = "DROP TABLE %s";

    public static final String CREATE_TBL_BREAK = "" +
        "CREATE TABLE " + TBL_BREAK + " (" +
            "id      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "jobId   INTEGER NOT NULL," +
            "start   INTEGER NOT NULL," +
            "end     INTEGER NOT NULL," +
            "comment TEXT," +
            "FOREIGN KEY (jobId) REFERENCES job(id)" +
        ");";

    public static final String CREATE_TBL_JOB = "" +
        "CREATE TABLE " + TBL_JOB + " (" +
            "id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "projectId   INTEGER NOT NULL," +
            "start       LONG NOT NULL," +
            "end         LONG NOT NULL," +
            "title       VARCHAR(64)," +
            "comment     TEXT," +
            "FOREIGN KEY (projectId) REFERENCES project(id)" +
        ");";

    public static final String CREATE_TBL_PROJECT = "" +
        "CREATE TABLE " + TBL_PROJECT + " (" +
            "id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "customerId  INTEGER NOT NULL," +
            "title       VARCHAR(64) NOT NULL," +
            "identifier  VARCHAR(64)," +
            "comment     TEXT," +
            "FOREIGN KEY (customerId) REFERENCES customer(id)" +
        ");";

    public static final String CREATE_TBL_CUSTOMER = "" +
        "CREATE TABLE " + TBL_CUSTOMER + " (" +
            "id      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
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

    /**
     * Drop all tables in this schema.
     */
    public void dropSchema() {
        db.beginTransaction();

        for (String table: TABLES) {
            db.execSQL(String.format(DROP_TABLE_TEMPLATE, table));
        }

        db.setTransactionSuccessful();
        db.endTransaction();
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
