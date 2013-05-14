package de.iweinzierl.timetracking.persistence;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import de.iweinzierl.timetracking.domain.Customer;
import de.iweinzierl.timetracking.domain.Job;
import de.iweinzierl.timetracking.domain.Project;
import de.iweinzierl.timetracking.exception.DatabaseException;
import de.iweinzierl.timetracking.persistence.db.SQLiteDB;
import de.iweinzierl.timetracking.persistence.db.SQLiteDatabaseCreator;
import de.iweinzierl.timetracking.persistence.repository.SQLiteRepository;

import java.util.Date;

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
        Project inserted = null;

        try {
            inserted = sqLiteRepository.save(project);
            throw new AssertionError("DatabaseException expected - constraint violation project.customerId");
        }
        catch (DatabaseException e) {
            assertTrue(inserted == null);
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
        }
        catch (DatabaseException e) {
        }
    }
}
