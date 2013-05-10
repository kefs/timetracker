package de.iweinzierl.timetracking.persistence.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.persistence.db.factory.BreakFactory;
import de.iweinzierl.timetracking.persistence.db.factory.CustomerFactory;
import de.iweinzierl.timetracking.persistence.db.factory.DomainFactory;
import de.iweinzierl.timetracking.persistence.db.factory.JobFactory;
import de.iweinzierl.timetracking.persistence.db.factory.ProjectFactory;
import de.iweinzierl.timetracking.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLiteDB extends SQLiteOpenHelper {

    public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version,
            DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabaseCreator creator = new SQLiteDatabaseCreator(db);
        creator.createSchema();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQLiteDatabaseCreator creator = new SQLiteDatabaseCreator(db);
        creator.updateSchema();
    }

    public List<Break> listBreaksByJob(long jobId) {
        SQLiteDatabase readableDB = getReadableDatabase();
        return new SQLiteHelper<Break>(readableDB, new BreakFactory()).listBy(SQLiteDatabaseCreator.CREATE_TBL_BREAK,
                SQLiteDatabaseCreator.COLS_BREAK, "job_id = ", new String[]{String.valueOf(jobId)});
    }

    public List<Job> listJobsByProject(long projectId) {
        SQLiteDatabase readableDB = getReadableDatabase();
        return new SQLiteHelper<Job>(readableDB, new JobFactory()).listBy(SQLiteDatabaseCreator.CREATE_TBL_JOB,
                SQLiteDatabaseCreator.COLS_JOB, "project_id = ", new String[]{String.valueOf(projectId)});
    }

    public List<Project> listProjectsByCustomer(long customerId) {
        SQLiteDatabase readableDB = getReadableDatabase();
        return new SQLiteHelper<Project>(readableDB, new ProjectFactory()).listBy(
                SQLiteDatabaseCreator.CREATE_TBL_PROJECT, SQLiteDatabaseCreator.COLS_PROJECT, "customer_id = ",
                new String[]{String.valueOf(customerId)});
    }

    public List<Customer> listCustomers() {
        SQLiteDatabase readableDB = getReadableDatabase();
        return new SQLiteHelper<Customer>(readableDB, new CustomerFactory()).listBy(
                SQLiteDatabaseCreator.TBL_CUSTOMER, SQLiteDatabaseCreator.COLS_CUSTOMER, null, null);
    }

    private class SQLiteHelper<T> {

        private SQLiteDatabase db;
        private DomainFactory<T> factory;

        public SQLiteHelper(SQLiteDatabase db, DomainFactory<T> factory) {
            this.db = db;
            this.factory = factory;
        }

        public List<T> listBy(String table, String[] columns, String where, String[] whereArgs) {
            Logger.debug(getClass(), String.format("Fetch database entities for table '%s'", table));
            Cursor cursor = db.query(table, columns, where, whereArgs, null, null, null);

            int count = cursor.getCount();
            if (count <= 0) {
                Logger.debug(getClass(), String.format("No object from table '%s' found.", table));
                return Collections.emptyList();
            }

            List<T> results = new ArrayList<T>(count);
            cursor.moveToFirst();

            do {
                T result = factory.create(cursor);
                if (result != null) {
                    results.add(result);
                }
            } while (cursor.moveToNext());


            return results;
        }
    }
}
