package de.iweinzierl.timetracking.persistence.repository;

import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;

import java.util.List;

public interface Repository {

    //
    // Customer related method definitions
    //

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(Customer customer);

    List<Customer> listCustomers();

    //
    // Project related method definitions
    //

    Project save(Project project);

    Project update(Project project);

    void delete(Project project);

    List<Project> listProjects(Customer customer);

    //
    // Job related method definitions
    //

    Job save(Job job);

    Job update(Job job);

    void delete(Job job);

    List<Job> listJobs(Project project);

    //
    // Break related method definitions
    //

    Break save(Break aBreak);

    Break update(Break aBreak);

    void delete(Break aBreak);

    List<Break> listBreaks(Job job);
}
