package de.iweinzierl.timetracking.persistence.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.exception.PropertyAccessException;
import de.iweinzierl.timetracking.persistence.db.factory.BreakFactory;
import de.iweinzierl.timetracking.persistence.db.factory.CustomerFactory;
import de.iweinzierl.timetracking.persistence.db.factory.DomainFactory;
import de.iweinzierl.timetracking.persistence.db.factory.JobFactory;
import de.iweinzierl.timetracking.persistence.db.factory.ProjectFactory;
import de.iweinzierl.timetracking.utils.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SQLiteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "timetracker.db";
    public static final int DATABASE_VERSION = 1;

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
        SQLiteDatabase readableDB = null;

        try {
            readableDB = getReadableDatabase();

            return new SQLiteHelper<Break>(readableDB, new BreakFactory()).listBy(SQLiteDatabaseCreator.TBL_BREAK,
                    SQLiteDatabaseCreator.COLS_BREAK, "jobId = $1", new String[]{String.valueOf(jobId)});
        } finally {
            if (readableDB != null) {
                readableDB.close();
            }
        }
    }

    public List<Job> listJobsByProject(long projectId) {
        SQLiteDatabase readableDB = null;
        List<Job> jobs = Lists.newArrayList();

        try {
            readableDB = getReadableDatabase();

            new SQLiteHelper<Job>(readableDB, new JobFactory()).listBy(SQLiteDatabaseCreator.TBL_JOB,
                    SQLiteDatabaseCreator.COLS_JOB, "projectId = $1", new String[]{String.valueOf(projectId)});
        } finally {
            if (readableDB != null) {
                readableDB.close();
            }
        }

        for (Job job : jobs) {
            job.setBreaks(listBreaksByJob(job.getId()));
        }

        return jobs;
    }

    public List<Project> listProjectsByCustomer(long customerId) {
        SQLiteDatabase readableDB = null;
        List<Project> projects = Lists.newArrayList();

        try {
            readableDB = getReadableDatabase();

            projects = new SQLiteHelper<Project>(readableDB, new ProjectFactory()).listBy(
                    SQLiteDatabaseCreator.TBL_PROJECT, SQLiteDatabaseCreator.COLS_PROJECT, "customerId = $1",
                    new String[]{String.valueOf(customerId)});
        } finally {
            if (readableDB != null) {
                readableDB.close();
            }
        }

        for (Project project : projects) {
            project.setJobs(listJobsByProject(project.getId()));
        }

        return projects;
    }

    public List<Customer> listCustomers() {
        SQLiteDatabase readableDB = null;
        List<Customer> customers = Lists.newArrayList();

        try {
            readableDB = getReadableDatabase();
            customers = new SQLiteHelper<Customer>(readableDB, new CustomerFactory()).listBy(
                    SQLiteDatabaseCreator.TBL_CUSTOMER, SQLiteDatabaseCreator.COLS_CUSTOMER, null, null);
        } finally {
            if (readableDB != null) {
                readableDB.close();
            }
        }

        for (Customer customer : customers) {
            customer.setProjects(listProjectsByCustomer(customer.getId()));
        }

        return customers;
    }

    public Break save(Break aBreak) throws DatabaseException {
        SQLiteDatabase writableDatabase = null;
        Integer newId = null;

        try {
            writableDatabase = getWritableDatabase();
            newId = new SQLiteHelper<Break>(writableDatabase, new BreakFactory()).save(
                    SQLiteDatabaseCreator.TBL_BREAK, aBreak);
        } finally {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
        }

        if (newId > 0) {
            aBreak.setId(newId);
        }

        return aBreak;
    }

    public Job save(Job job) throws DatabaseException {
        SQLiteDatabase writableDatabase = null;
        Integer newId = null;

        try {
            writableDatabase = getWritableDatabase();
            newId = new SQLiteHelper<Job>(writableDatabase, new JobFactory()).save(SQLiteDatabaseCreator.TBL_JOB,
                job);
        }
        finally {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
        }

        if (newId > 0) {
            job.setId(newId);
        }

        for (Break aBreak : job.getBreaks()) {
            aBreak.setJobId(newId);
            save(aBreak);
        }

        return job;
    }

    public Project save(Project project) throws DatabaseException {
        SQLiteDatabase writableDatabase = null;
        Integer newId = null;

        try {
            writableDatabase = getWritableDatabase();
            newId = new SQLiteHelper<Project>(writableDatabase, new ProjectFactory()).save(
                SQLiteDatabaseCreator.TBL_PROJECT, project);
        }
        finally {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
        }

        if (newId > 0) {
            project.setId(newId);
        }

        for (Job job : project.getJobs()) {
            job.setProjectId(newId);
            save(job);
        }

        return project;
    }

    public Customer save(Customer customer) throws DatabaseException {
        SQLiteDatabase writableDatabase = null;
        Integer newId = null;

        try {
            writableDatabase = getWritableDatabase();
            newId = new SQLiteHelper<Customer>(writableDatabase, new CustomerFactory()).save(
                SQLiteDatabaseCreator.TBL_CUSTOMER, customer);
        }
        finally {
            if (writableDatabase != null) {
                writableDatabase.close();
            }
        }

        if (newId > 0) {
            customer.setId(newId);
        }

        for (Project project : customer.getProjects()) {
            project.setCustomerId(newId);
            Project proj = save(project);
            if (proj != null) {
                project.setId(proj.getId());
            }

        }

        return customer;
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

        public int save(String table, T entity) throws DatabaseException {
            Logger.debug(getClass(), String.format("Save entity '%s' to table '%s'", entity.getClass(), table));

            try {
                db.beginTransaction();
                int insert = (int) db.insertOrThrow(table, null, createValues(entity));
                db.setTransactionSuccessful();
                db.endTransaction();

                return insert;
            } catch (Exception e) {
                throw new DatabaseException(String.format("Unable to save %s to table '%s'", entity, table), e);
            }
        }

        protected ContentValues createValues(T entity) {
            ContentValues values = new ContentValues();
            Field[] fields = entity.getClass().getDeclaredFields();

            try {
                for (Field field : fields) {
                    String name = field.getName();
                    Type type = field.getType();

                    if (type == Integer.class) {
                        putIntProperty(entity, name, values);
                    } else if (type == Long.class) {
                        putLongProperty(entity, name, values);
                    } else if (type == String.class) {
                        putStringProperty(entity, name, values);
                    } else if (type == Date.class) {
                        putDateProperty(entity, name, values);
                    } else {
                        Logger.warn(getClass(),
                                String.format("Skip property '%s' of type '%s'", name, type.getClass()));
                    }
                }
            } catch (PropertyAccessException e) {
                Logger.warn(getClass(), e.getMessage());
            }

            return values;
        }

        private void putIntProperty(T entity, String field, ContentValues values) throws PropertyAccessException {
            Integer value = getIntProperty(entity, field);
            if (value != null) {
                values.put(field, value);
            }
        }

        private void putLongProperty(T entity, String field, ContentValues values) throws PropertyAccessException {
            Long value = getLongProperty(entity, field);
            if (value != null) {
                values.put(field, value);
            }
        }

        private void putStringProperty(T entity, String field, ContentValues values) throws PropertyAccessException {
            String value = getStringProperty(entity, field);
            if (value != null && !Strings.isNullOrEmpty(value)) {
                values.put(field, value);
            }
        }

        private void putDateProperty(T entity, String field, ContentValues values) throws PropertyAccessException {
            Long value = getDateProperty(entity, field);
            if (value != null) {
                values.put(field, value);
            }
        }

        protected Object getProperty(T entity, String field) throws PropertyAccessException {
            String getterStr = String.format("get%s%s", field.substring(0, 1).toUpperCase(),
                    field.substring(1, field.length()));

            try {
                Method getter = entity.getClass().getDeclaredMethod(getterStr);
                return getter.invoke(entity);
            } catch (NoSuchMethodException e) {
                throw new PropertyAccessException("Getter '" + getterStr + "' not existing.", e);
            } catch (InvocationTargetException e) {
                throw new PropertyAccessException("Getter '" + getterStr + "' not invokable.", e);
            } catch (IllegalAccessException e) {
                throw new PropertyAccessException("Getter '" + getterStr + "' not invokable.", e);
            }
        }

        protected Integer getIntProperty(T entity, String field) throws PropertyAccessException {
            Object value = getProperty(entity, field);
            return value != null ? Integer.valueOf(String.valueOf(value)) : null;
        }

        protected Long getLongProperty(T entity, String field) throws PropertyAccessException {
            Object value = getProperty(entity, field);
            return value != null ? Long.valueOf(String.valueOf(value)) : null;
        }

        protected String getStringProperty(T entity, String field) throws PropertyAccessException {
            Object value = getProperty(entity, field);
            return value != null ? String.valueOf(value) : null;
        }

        protected Long getDateProperty(T entity, String field) throws PropertyAccessException {
            Object value = getProperty(entity, field);
            return value != null ? ((Date) value).getTime() : null;
        }
    }
}
