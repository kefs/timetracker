package de.iweinzierl.timetracking.persistence.repository;

import android.content.Context;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;

import java.util.List;

public class SQLiteRepository implements Repository {

    private Context context;

    public SQLiteRepository(Context context) {
        this.context = context;
    }

    @Override
    public Customer save(Customer customer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Project save(Project project) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
    public List<Project> listProjects(Customer customer) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Job save(Job job) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Break save(Break aBreak) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
