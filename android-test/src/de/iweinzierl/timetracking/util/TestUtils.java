package de.iweinzierl.timetracking.util;

import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.CustomerBuilder;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.domain.ProjectBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TestUtils {

    public static Customer createCustomer(String title) {
        CustomerBuilder builder = new CustomerBuilder();
        builder.setName(title);

        return builder.build();
    }

    public static Customer createCustomerWithProjects(String title, int projectCount) {
        List<Project> projects = new ArrayList<Project>();

        for (int i = 0; i < projectCount; i++) {
            projects.add(createProject("New Project " + i));
        }

        CustomerBuilder builder = new CustomerBuilder();
        builder.setName(title);
        builder.setProjects(projects);

        return builder.build();
    }

    public static Customer createCustomerWithProjectsAndJobs(String title, int projectCount, int jobCount) {
        List<Project> projects = new ArrayList<Project>();

        for (int i = 0; i < projectCount; i++) {
            Project project = createProject("New Project " + i);

            for (int j = 0; j < jobCount; j++) {
                project.addJob(createJob("New Job " + j, j));
            }

            projects.add(project);
        }

        CustomerBuilder builder = new CustomerBuilder();
        builder.setName(title);
        builder.setProjects(projects);

        return builder.build();
    }

    public static Customer createCustomerWithProjectsAndJobsAndBreaks(String title, int projectCount, int jobCount,
            int breakCount) {

        List<Project> projects = new ArrayList<Project>();

        for (int i = 0; i < projectCount; i++) {
            Project project = createProject("New Project " + i);

            for (int j = 0; j < jobCount; j++) {
                Job job = createJob("New Job " + j, j);

                for (int k = 0; k < breakCount; k++) {
                    job.addBreak(createBreak(k));
                }

                project.addJob(job);
            }

            projects.add(project);
        }

        CustomerBuilder builder = new CustomerBuilder();
        builder.setName(title);
        builder.setProjects(projects);

        return builder.build();
    }

    public static Project createProject(String title) {
        ProjectBuilder builder = new ProjectBuilder();
        builder.setTitle(title);

        return builder.build();
    }

    public static Job createJob(String title, int duration) {
        Date[] dates = createStartEndDates(duration);

        Job job = new Job();
        job.setTitle(title);
        job.setStart(dates[0]);
        job.setEnd(dates[1]);

        return job;
    }

    public static Break createBreak(int duration) {
        Date[] dates = createStartEndDates(duration);
        return new Break(dates[0], dates[1]);
    }

    public static Date[] createStartEndDates(int duration) {
        Calendar cal = new GregorianCalendar();

        Date end = cal.getTime();
        cal.add(Calendar.MINUTE, duration * -1);

        return new Date[] { cal.getTime(), end};
    }
}
