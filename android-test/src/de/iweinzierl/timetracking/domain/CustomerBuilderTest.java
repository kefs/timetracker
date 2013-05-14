package de.iweinzierl.timetracking.domain;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class CustomerBuilderTest extends TestCase {

    public void testBuildWithoutProjects() throws Exception {
        CustomerBuilder builder = new CustomerBuilder();
        builder.setName("New customer");

        Customer customer = builder.build();
        assertNotNull(customer);
        assertEquals("New customer", customer.getName());
        assertNotNull(customer.getProjects());
        assertEquals(0, customer.getProjects().size());
    }

    public void testBuildWithProjects() throws Exception {
        List<Project> projects = new ArrayList<Project>();
        projects.add(new Project("Project 1"));
        projects.add(new Project("Project 2"));
        projects.add(new Project("Project 3"));

        CustomerBuilder builder = new CustomerBuilder();
        builder.setName("New Customer with Projects");
        builder.setProjects(projects);

        Customer customer = builder.build();
        assertNotNull(customer);
        assertEquals("New Customer with Projects", customer.getName());
        assertNotNull(customer.getProjects());
        assertEquals(3, customer.getProjects().size());
    }

    public void testBuildWithoutName() throws Exception {
        CustomerBuilder builder = new CustomerBuilder();

        try {
            builder.build();
            throw new AssertionError("Expected NullPointerException because of missing customer 'name'");
        }
        catch (NullPointerException e) {
        }
    }
}
