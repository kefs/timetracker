package de.iweinzierl.timetracking.persistence.repository;

import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;

import java.util.List;

public interface Repository {

    //
    // Customer related method definitions
    //

    Customer save(Customer customer) throws DatabaseException;

    Customer update(Customer customer);

    void delete(Customer customer);

    List<Customer> listCustomers();

    //
    // Project related method definitions
    //

    Project save(Project project) throws DatabaseException;

    Project update(Project project);

    void delete(Project project);

    List<Project> listProjects(int customerId);

    //
    // Job related method definitions
    //

    Job save(Job job) throws DatabaseException;

    Job update(Job job);

    void delete(Job job);

    List<Job> listJobs(Project project);

    //
    // Break related method definitions
    //

    Break save(Break aBreak) throws DatabaseException;

    Break update(Break aBreak);

    void delete(Break aBreak);

    List<Break> listBreaks(Job job);
}
