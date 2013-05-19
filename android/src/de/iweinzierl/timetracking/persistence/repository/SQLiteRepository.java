package de.iweinzierl.timetracking.persistence.repository;

import android.content.Context;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.db.SQLiteDB;

import java.util.List;

public class SQLiteRepository implements Repository {

    private Context context;

    private SQLiteDB sqliteDB;

    public SQLiteRepository(Context context) {
        this.context = context;
        this.sqliteDB = new SQLiteDB(context, SQLiteDB.DATABASE_NAME, null, SQLiteDB.DATABASE_VERSION);
    }

    @Override
    public Customer save(Customer customer) throws DatabaseException {
        return sqliteDB.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Customer customer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Customer> listCustomers() {
        return sqliteDB.listCustomers();
    }

    @Override
    public Project save(Project project) throws DatabaseException {
        return sqliteDB.save(project);
    }

    @Override
    public Project update(Project project) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Project project) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Project> listProjects(int customerId) {
        return sqliteDB.listProjectsByCustomer(customerId);
    }

    @Override
    public Job save(Job job) throws DatabaseException {
        return sqliteDB.save(job);
    }

    @Override
    public Job update(Job job) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Job job) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Job> listJobs(Project project) {
        return sqliteDB.listJobsByProject(project.getId());
    }

    @Override
    public Break save(Break aBreak) throws DatabaseException {
        return sqliteDB.save(aBreak);
    }

    @Override
    public Break update(Break aBreak) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Break aBreak) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Break> listBreaks(Job job) {
        return sqliteDB.listBreaksByJob(job.getId());
    }
}
