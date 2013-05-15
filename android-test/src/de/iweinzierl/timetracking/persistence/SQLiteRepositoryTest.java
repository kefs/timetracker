package de.iweinzierl.timetracking.persistence;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import de.iweinzierl.timetracking.domain.Break;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.db.SQLiteDB;
import de.iweinzierl.timetracking.persistence.db.SQLiteDatabaseCreator;
import de.iweinzierl.timetracking.persistence.repository.SQLiteRepository;
import de.iweinzierl.timetracking.util.TestUtils;

import java.util.Date;
import java.util.List;

public class SQLiteRepositoryTest extends AndroidTestCase {

    private SQLiteRepository sqLiteRepository;

    public void setUp() throws Exception {
        super.setUp();

        Context baseContext = getContext();
        RenamingDelegatingContext context = new RenamingDelegatingContext(baseContext, "test_");

        SQLiteDB sqLiteDB = new SQLiteDB(context, SQLiteDB.DATABASE_NAME, null, SQLiteDB.DATABASE_VERSION);
        SQLiteDatabaseCreator dbCreator = new SQLiteDatabaseCreator(sqLiteDB.getWritableDatabase());
        dbCreator.dropSchema();
        dbCreator.createSchema();

        sqLiteRepository = new SQLiteRepository(context);
    }

    public void testSaveCustomer() throws Exception {
        Customer customer = new Customer("New Customer");
        Customer inserted = sqLiteRepository.save(customer);

        assertEquals("New Customer", inserted.getName());
        assertTrue("Customer ID must be > 0 when saved to database", inserted.getId() > 0);
    }

    public void testSaveProject() throws Exception {
        Customer customer = new Customer("New Customer with Project");
        Project project = new Project("New Project");

        customer = sqLiteRepository.save(customer);
        customer.addProject(project);

        project = sqLiteRepository.save(project);

        assertTrue("Project ID must be > 0 when saved to database", project.getId() > 0);
        assertEquals("New Project", project.getTitle());
    }

    public void testSaveProjectWithoutCustomer() throws Exception {
        Project project = new Project("New Project without Customer");

        try {
            sqLiteRepository.save(project);
            throw new AssertionError("DatabaseException expected - constraint violation project.customerId");
        } catch (DatabaseException e) {
        }
    }

    public void testSaveJob() throws Exception {
        Customer customer = new Customer("New Customer with projects and jobs");
        Project project = new Project("New Project with jobs");

        sqLiteRepository.save(customer);
        customer.addProject(project);
        sqLiteRepository.save(project);

        Job job = new Job();
        job.setStart(new Date());
        job.setEnd(new Date());
        job.setTitle("New Job");

        project.addJob(job);
        Job inserted = sqLiteRepository.save(job);

        assertNotNull(inserted);
        assertNotNull(inserted.getId());
        assertEquals(job.getTitle(), inserted.getTitle());
    }

    public void testJobWithoutProject() throws Exception {
        Job job = new Job();
        job.setTitle("New Job without Project");
        job.setStart(new Date());
        job.setEnd(new Date());

        try {
            sqLiteRepository.save(job);
            throw new AssertionError("Expected DatabaseException - constraint violation job.projectId");
        } catch (DatabaseException e) {
        }
    }

    public void testListCustomers() throws Exception {
        int count = 5;

        for (int i = 0; i < 5; i++) {
            sqLiteRepository.save(TestUtils.createCustomer("New Customer " + i));
        }

        List<Customer> customerList = sqLiteRepository.listCustomers();
        assertNotNull(customerList);
        assertEquals(count, customerList.size());
    }

    public void testListProjects() throws Exception {
        Customer customer = TestUtils.createCustomerWithProjects("New Customer With Projects", 4);
        Customer inserted = sqLiteRepository.save(customer);

        List<Project> projects = sqLiteRepository.listProjects(inserted);
        assertNotNull(projects);
        assertEquals(4, projects.size());

        for (Project project: projects) {
            assertNotNull(project.getId());
            assertNotNull(project.getCustomerId());
        }
    }

    public void testListJobs() throws Exception {
        Customer customer = TestUtils.createCustomerWithProjectsAndJobs("New Customer With Projects and Jobs", 4, 3);
        Customer inserted = sqLiteRepository.save(customer);

        List<Project> projects = sqLiteRepository.listProjects(inserted);
        assertNotNull(projects);
        assertTrue(projects.size() > 0);

        for (Project project: projects) {
            List<Job> jobs = sqLiteRepository.listJobs(project);
            assertNotNull(jobs);
            assertTrue(jobs.size() > 0);

            for (Job job: jobs) {
                assertNotNull(job.getId());
                assertNotNull(job.getProjectId());
            }
        }
    }

    public void testListBreaks() throws Exception {
        Customer customer = TestUtils.createCustomerWithProjectsAndJobsAndBreaks(
                "New Customer with Projects and Jobs" + " and Breaks", 3, 4, 2);

        Customer inserted = sqLiteRepository.save(customer);

        List<Project> projects = sqLiteRepository.listProjects(inserted);
        assertNotNull(projects);
        assertTrue(projects.size() == 3);

        for (Project project: projects) {
            List<Job> jobs = sqLiteRepository.listJobs(project);
            assertNotNull(jobs);
            assertTrue(jobs.size() == 4);

            for (Job job: jobs) {
                assertNotNull(job.getId());
                assertNotNull(job.getProjectId());

                List<Break> breaks = sqLiteRepository.listBreaks(job);
                assertNotNull(breaks);
                assertTrue(breaks.size() == 2);

                for (Break aBreak: breaks) {
                    assertNotNull(aBreak.getId());
                    assertNotNull(aBreak.getJobId());
                }
            }
        }
    }
}
