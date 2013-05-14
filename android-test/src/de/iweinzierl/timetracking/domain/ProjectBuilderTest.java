package de.iweinzierl.timetracking.domain;

import de.iweinzierl.timetracking.util.TestUtils;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class ProjectBuilderTest extends TestCase {

    public void testBuild() throws Exception {
        ProjectBuilder builder = new ProjectBuilder();
        builder.setTitle("New Project");

        Project project = builder.build();
        assertNotNull(project);
        assertEquals("New Project", project.getTitle());
    }

    public void testBuildWithoutTitle() throws Exception {
        ProjectBuilder builder = new ProjectBuilder();

        try {
            builder.build();
            throw new AssertionError("Expected NullPointerException because of missing title.");
        } catch (NullPointerException e) {
        }
    }

    public void testBuildWithAllFields() throws Exception {
        ProjectBuilder builder = new ProjectBuilder();
        builder.setTitle("New Project");
        builder.setComment("New Comment");
        builder.setIdentifier("New Identifier");

        Project project = builder.build();
        assertNotNull(project);
        assertEquals("New Project", project.getTitle());
        assertEquals("New Comment", project.getComment());
        assertEquals("New Identifier", project.getIdentifier());
    }

    public void testBuildWithJobs() throws Exception {
        List<Job> jobs = new ArrayList<Job>();
        jobs.add(TestUtils.createJob("New Job 1", 2));
        jobs.add(TestUtils.createJob("New Job 2", 4));
        jobs.add(TestUtils.createJob("New Job 3", 6));

        ProjectBuilder builder = new ProjectBuilder();
        builder.setTitle("New Project with Jobs");
        builder.setJobs(jobs);

        Project project = builder.build();

        assertNotNull(project);
        assertNotNull(project.getJobs());
        assertEquals(3, project.getJobs().size());
    }

}
