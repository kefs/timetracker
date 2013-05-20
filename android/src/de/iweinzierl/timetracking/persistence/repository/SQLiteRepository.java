package de.iweinzierl.timetracking.persistence.repository;

import android.content.Context;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.db.SQLiteDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteRepository implements Repository {

    private static SQLiteRepository INSTANCE;

    private SQLiteDB sqliteDB;

    private List<Customer> customers;
    private Map<Integer, List<Project>> projects;

    public SQLiteRepository() {
        this.projects = new HashMap<Integer, List<Project>>(5);
    }

    public static SQLiteRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteRepository();
        }

        INSTANCE.connect(context);
        return INSTANCE;
    }

    private void connect(Context context) {
        this.sqliteDB = new SQLiteDB(context, SQLiteDB.DATABASE_NAME, null, SQLiteDB.DATABASE_VERSION);
    }

    @Override
    public Customer save(Customer customer) throws DatabaseException {
        Customer inserted = sqliteDB.save(customer);

        if (inserted != null && inserted.getId() != null && inserted.getId() > 0) {
            synchronized (customers) {
                customers.add(inserted);
            }
        }

        return inserted;
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
        if (customers == null) {
            customers = sqliteDB.listCustomers();
        }

        return customers;
    }

    @Override
    public Project save(Project project) throws DatabaseException {
        Project inserted = sqliteDB.save(project);
        if (inserted.getId() != null && inserted.getId() > 0) {

            synchronized (this.projects) {
                List<Project> projects = this.projects.get(inserted.getCustomerId());
                if (projects == null) {
                    projects = new ArrayList<Project>(1);
                    this.projects.put(inserted.getCustomerId(), projects);
                }

                projects.add(inserted);
            }
        }

        return inserted;
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
        List<Project> projects = this.projects.get(customerId);
        if (projects == null) {
            projects = sqliteDB.listProjectsByCustomer(customerId);
            this.projects.put(customerId, projects);
        }

        return projects;
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
