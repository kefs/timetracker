package de.iweinzierl.timetracking.domain;

import com.google.common.base.Preconditions;
import de.iweinzierl.timetracking.utils.Logger;

import java.io.Serializable;
import java.util.List;

/**
 * A representation of a customer. A Customer consists of a name  and a list of projects.
 */
public class Customer implements Serializable {

    private long id;

    private String name;

    private List<Project> projects;

    /**
     * Create a new Customer with name.
     *
     * @param name The name of the new Customer. May not be null or an empty string.
     *
     * @throws NullPointerException if <i>name</i> is null.
     * @throws IllegalArgumentException if <i>name</i> has zero length.
     */
    public Customer(String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(name.length() > 0);

        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * Add a new {@link Project} to this Customer.
     *
     * @param project The new {@link Project} for this Customer. May not be null, otherwise it is not added.
     *
     * @return the <i>project</i> that have been added or null, if nothing was added to this Customer.
     */
    public Project addProject(Project project) {
        if (project != null) {
            projects.add(project);
            return project;
        }

        return null;
    }

    /**
     * Calculates the duration of {@link Project}s for this Customer.
     *
     * @return the total duration of {@Project}s for this Customer in milliseconds.
     */
    public long calculateDuration() {
        long total = 0l;

        for (Project p: getProjects()) {
            try {
                total += p.calculateDuration();
            }
            catch (IllegalStateException e) {
                Logger.warn(this.getClass(), String.format("Unable to include duration of %s to total duration of " +
                        "Customer.", p.getTitle()));
            }
        }

        Logger.debug(this.getClass(), String.format("Total duration of %s is %s ms", getName(), total));
        return total;
    }
}
